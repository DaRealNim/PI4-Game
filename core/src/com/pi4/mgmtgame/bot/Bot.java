package com.pi4.mgmtgame.bot;

import com.pi4.mgmtgame.Inventory;
import com.pi4.mgmtgame.Map;
import com.pi4.mgmtgame.networking.Server;
import com.pi4.mgmtgame.resources.Resources;
import com.pi4.mgmtgame.resources.Plant;
import com.pi4.mgmtgame.resources.Grain;
import com.pi4.mgmtgame.blocks.*;
import com.pi4.mgmtgame.blocks.Field;
import com.pi4.mgmtgame.resources.TreeSeeds;

import java.util.ArrayList;

public class Bot {

  class Coord {
    final int x;
    final int y;

    Coord(int x, int y)
    {
      this.x = x;
      this.y = y;
    }

    @Override
    public String toString() {
        return "("+x+", "+y+")";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Coord) {
            Coord objCoord = (Coord)obj;
            return (objCoord.x == this.x && objCoord.y == this.y);
        } else {
            return false;
        }
    }
  }

  private ArrayList<Coord> ownedStructures;
  private ArrayList<Coord> ownedTerrains;
  private ArrayList<Coord> ownedLakes;
  private ArrayList<Coord> goodFieldSpots;
  private Map map;
  private Inventory inv;
  private int botID;
  private HQ botHQ;
  private Server server;

//Respect clean code rules and indentation I'm tired of the code looking like a slum
//if I see another "if" tree I'm going insane.

  public Bot(Map m, Inventory i, int id, Server sv, int HQx, int HQy)
  {
    map = m;
    inv = i;
    botID = id;
    server = sv;
    goodFieldSpots = scanForPlainsNearLakes();
    ownedTerrains = new ArrayList<Coord>();
    ownedStructures = new ArrayList<Coord>();

    botHQ = new HQ(HQx, HQy);
    botHQ.setOwnerID(botID);
    map.setStructAt(HQx, HQy, botHQ);

    Coord HQ = new Coord(HQx, HQy);

    for (int x = -1; x <= 1; x++)
    {
      for (int y = -1; y <= 1; y++)
      {
        if (x != 0 || y != 0)
        {
          if (map.getStructAt(HQ.x + x, HQ.y + y) != null)
            ownedStructures.add(new Coord(HQ.x + x, HQ.y + y));
          ownedTerrains.add(new Coord(HQ.x + x, HQ.y + y));
        }
      }
    }
    System.out.println(ownedStructures);
  }

  public void play() //This is the most abstract function, so it is at the top
  {
    //start turn
    System.out.println("Bot start");
    harvestFields();
    sellPlants();
    if (allTerrainsUsed())
      buyTerrains();
    buildStructures();
    buySeeds();
    plantSeeds();
    printState();
    System.out.println("Bot end");
    //sabotage()
    //end
  }

  public void printState()
  {
    for (Resources x : inv.getRessources())
    {
      System.out.println(x.toString() + ": " + x.getVolume());
    }
    System.out.println(botID + " has " + inv.getMoney() + "$");
  }

  public Inventory getInventory() {
      return inv;
  }

  public int getBotID() {
      return botID;
  }

  private void harvestFields() //Second most abstract, so on.
  {
    ArrayList<Coord> ownedFields = getOwnedFields();

    for (Coord c : ownedFields)
    {
        Plant harvested;
        Field currField = (Field) map.getStructAt(c.x, c.y);

        if (canHarvest(currField) || currField instanceof TreeField)
        {
          harvested = currField.harvest();
          harvested.addVolume(4);

          if (currField instanceof TreeField)
          {
            map.setStructAt(c.x, c.y, null);
            ownedStructures.remove(c); // could've spent my life trying to find this
          }

          inv.addPlant(harvested.getId(), harvested.getVolume());
          System.out.println("Bot " + botID + " harvested " + harvested.toString() + " at " + c.x + ", " + c.y);
        }
    }
  }

  private void sellPlants()
  {
    for (Plant p : inv.getPlants())
    {
      int numberOfPlants = p.getVolume();

      if (priceIsHigherThanMarketAverage(p)
      && numberOfPlants > 0
      && p.getPrice() > 0)
      {
        System.out.println("Bot " + botID + " sold " + p.toString() + " at " + p.getPrice());
        server.sellPlant(p, p.getVolume());
      }
    }
  }

  private void buyTerrains()
  {
    int initialFunds = inv.getMoney();
    ArrayList<Coord> buyablePlains = getBuyablePlains();
    ArrayList<Coord> buyableLakes = getBuyableLakes();

    while (inv.getMoney() > initialFunds / 3 && inv.getMoney() >= 1100) //This condition will be changed to something more elegant when there are maintenance costs.
    {
      for (Coord c : buyableLakes)
      {
        buyTerrainAt(c);
      }

      for (Coord c : buyablePlains)
      {
        buyTerrainAt(c);
      }
    }
  }

  private void buildStructures() //TODO: buildPastures()
  {
      buildFields();
      buildPastures();
  }

  private void buySeeds() //TODO: optimize seed buying by using seed prices
  {
    int ownedFields = getOwnedFields().size();
    int boughtGrains = inv.getSeeds()[0].getVolume() + inv.getSeeds()[1].getVolume() + inv.getSeeds()[2].getVolume() + inv.getSeeds()[3].getVolume();

    System.out.println("ownedFields:" + getOwnedFields());
    System.out.println("ownedStructures:" + ownedStructures);

    for (Grain g : inv.getSeeds())
    {
      while(priceIsLowerThanMarketAverage(g) &&
            boughtGrains < ownedFields ) //TODO: failsafe this so the bot doesn't bankrupt itself
      {
        System.out.println("Bot " + botID + " bought " + g.toString() + " at " + g.getPrice());
        server.buyGrain(g, 1);
        ++boughtGrains;
      }
    }
  }

  private void plantSeeds()
  {
    ArrayList<Coord> ownedFields = getOwnedFields();
    Field currField;

    for (Coord c : ownedFields)
    {
      currField = (Field) map.getStructAt(c.x, c.y);
      for (Grain g : inv.getSeeds())
      {
        if (g instanceof TreeSeeds)
            continue;
        if (!currField.hasSeed() && inv.hasGrain(g))
        {
          currField.plantSeed(g);
          inv.removeGrain(g.getId(), 1);
        }
      }
    }
  }

  private void buildFields()
  {
    int initialFunds = inv.getMoney();
    for (Coord c : ownedTerrains)
    {
        System.out.println("Money: " + inv.getMoney());
        if (inv.getMoney() < 2500)
            break;
        buildFieldAt(c);
    }
  }

  private void buildPastures()
  {
    int initialFunds = inv.getMoney();

    for (Coord c : ownedTerrains)
    {
        System.out.println("Money: " + inv.getMoney());

        if (inv.getMoney() < 1200)
            break;

        if(!thereIsLakeNear(c.x, c.y))
          buildPastureAt(c);
    }
  }

  private void buyTerrainAt(Coord c)
  {
    Environment terrain = map.getEnvironmentAt(c.x, c.y);
    int terrainCost = terrain.getPrice();

    if (canBuyTerrainAt(c))
    {
      inv.giveMoney(terrainCost);
      terrain.setOwnerID(botID);

      if (isLake(c))
        ownedLakes.add(c);
      else
        ownedTerrains.add(c);

      System.out.println("Bot " + botID + " bought terrain at " + c.x + ", " + c.y);
    }
  }

  private void buildFieldAt(Coord c)
  {
    Environment terrain = map.getEnvironmentAt(c.x, c.y);
    Field newField = new Field(c.x, c.y);
    int fieldCost = newField.getConstructionCost();

    if (!ownedStructures.contains(c)
        && fieldCost < inv.getMoney()
        && map.getStructAt(c.x, c.y) == null
        && !isLake(c))
    {
      inv.giveMoney(fieldCost);
      map.setStructAt(c.x, c.y, newField);
      newField.setOwnerID(botID);
      ownedStructures.add(c);

      System.out.println("Bot " + botID + " built field at " + c.x + ", " + c.y);
    }
  }

  private void buildPastureAt(Coord c)
  {
    Environment terrain = map.getEnvironmentAt(c.x, c.y);
    Pasture newPasture = new Pasture(c.x, c.y);
    int pastureCost = newPasture.getConstructionCost();

    if (!ownedStructures.contains(c)
        && pastureCost < inv.getMoney()
        && map.getStructAt(c.x, c.y) == null
        && !isLake(c))
    {
      inv.giveMoney(pastureCost);
      map.setStructAt(c.x, c.y, newPasture);
      newPasture.setOwnerID(botID);
      ownedStructures.add(c);

      System.out.println("Bot " + botID + " built pasture at " + c.x + ", " + c.y);
    }
  }

  //This function will be modified when trade routes are implemented
  private ArrayList<Coord> scanForPlainsNearLakes()
  {
    ArrayList<Coord> goodFieldSpots = new ArrayList();

    for (int x = 0; x < map.getMapHeight(); x++)
    {
      for (int y = 0; y < map.getMapWidth(); y++)
      {
        Environment currBlock = map.getEnvironmentAt(x, y);

        if (currBlock instanceof Plain && thereIsLakeNear(x, y))
          goodFieldSpots.add(new Coord(x, y));
      }
    }

    return (goodFieldSpots);
  }

  private ArrayList<Coord> getBuyablePlains()
  {
    ArrayList<Coord> buyablePlains = new ArrayList<Coord>();

    for (Coord c : ownedTerrains)
    {
      for (int x = -1; x < 1; x++)
      {
        for (int y = -1; y < 1; y++)
        {
          Coord nextBlock = new Coord(c.x + x, c.y + y);

          if (botOwnsAllTerrainAround(nextBlock))
            break;
          else if (!botOwnsTerrain(nextBlock) && isPlain(nextBlock))
            buyablePlains.add(c);
        }
      }
    }
    return (buyablePlains);
  }

  private ArrayList<Coord> getBuyableLakes()
  {
    ArrayList<Coord> buyableLakes = new ArrayList<Coord>();

    for (Coord c : ownedTerrains)
    {
      for (int x = -1; x < 1; x++)
      {
        for (int y = -1; y < 1; y++)
        {
          Coord nextBlock = new Coord(c.x + x, c.y + y);

          if (botOwnsAllTerrainAround(nextBlock))
            break;
          else if (!botOwnsTerrain(nextBlock) && isLake(nextBlock))
            buyableLakes.add(c);
        }
      }
    }
    return (buyableLakes);
  }

  private ArrayList<Coord> getOwnedFields()
  {
    ArrayList<Coord> ownedFields = new ArrayList();

    for (Coord c : ownedStructures)
    {
      Structure currStruct = map.getStructAt(c.x, c.y);
      if (structIsField(currStruct))
        ownedFields.add(c);
    }

    return (ownedFields);
  }

  private int totalFieldTerrainCost(ArrayList<Coord> spots)
  {
    Environment currBlockEnv;
    int totalCost = 0;

    for (Coord c : spots)
    {
      currBlockEnv = map.getEnvironmentAt(c.x, c.y);

      if (currBlockEnv.testOwner(-1)) //if terrain has no owner
        totalCost += currBlockEnv.getPrice();
    }

    return (totalCost);
  }

  /*public int totalMaintenanceCost(ArrayList<Coord> owned) //for future use.
  {
    return (0)
  }*/

  private int priceAverage(Inventory inv)
  {
    int sumOfPrices = 0;
    int resCount = 0;

    for (Plant p : inv.getPlants())
    {
      sumOfPrices += p.getPrice();
      ++resCount;
    }

    for (Grain g : inv.getSeeds())
    {
      sumOfPrices += g.getPrice();
      ++resCount;
    }

    return (sumOfPrices / resCount);
  }

  private boolean thereIsLakeNear(int x, int y)
  {
    Coord blockToCheck;
    for (int hor = -1; hor <= 1; hor++)
    {
      for (int vert = -1; vert <= 1; vert++)
      {
        blockToCheck = new Coord(x + hor, y + vert);
        if (!isLake(blockToCheck))
          return (true);
      }
    }
    return (false);
  }

  //I know this function also exists in Server but it's only for players since the server's invArray doesn't account for the inventory of bots.
  private boolean canBuyTerrainAt(Coord c)
  {
    Structure currBlockStruct = map.getStructAt(c.x, c.y);
    Environment currBlockEnv = map.getEnvironmentAt(c.x, c.y);
    int terrainPrice = currBlockEnv.getPrice();
    int availableMoney = inv.getMoney();

    if (currBlockStruct != null && availableMoney >= terrainPrice)
      return (true);
    else if (currBlockEnv.testOwner(-1) && availableMoney >= terrainPrice)
      return (true);

    return (false);
  }

  private boolean botOwnsAllTerrainAround(Coord c)
  {
    Environment terrainCheck;
    for (int hor = -1; hor <= 1; hor++)
    {
      for (int vert = -1; vert <= 1; vert++)
      {
        terrainCheck = map.getEnvironmentAt(c.x + hor, c.y + vert);
        if (terrainCheck != null)
        {
          if (!terrainCheck.testOwner(botID))
            return (false);
        }
      }
    }

    return (true);
  }

  private boolean botOwnsTerrain(Coord c)
  {
    Environment terrainCheck = map.getEnvironmentAt(c.x, c.y);
    if (terrainCheck != null)
    {
      if (terrainCheck.testOwner(botID))
        return (true);
    }
    return (false);
  }


  private boolean allTerrainsUsed()
  {
    for (Coord c : ownedTerrains)
    {
      if (map.getStructAt(c.x, c.y) == null)
        return (false);
    }
    return (true);
  }

  private boolean priceIsHigherThanMarketAverage(Resources r)
  {
    if (r.getPrice() >= priceAverage(inv))
      return (true);
    return (false);
  }

  private boolean priceIsLowerThanMarketAverage(Resources r)
  {
    System.out.println(r.getPrice() + " < " + priceAverage(inv));
    if (r.getPrice() < priceAverage(inv)*2)
      return (true);
    return (false);
  }

  private boolean structIsField(Structure structBlock)
  {
    return (structBlock instanceof Field && structBlock != null);
  }

  private boolean canHarvest(Field field)
  {
    return (field.hasSeedGrown());
  }

  private boolean isLake(Coord c)
  {
    return (map.getEnvironmentAt(c.x, c.y) instanceof Lake);
  }

  private boolean isPlain(Coord c)
  {
    return (map.getEnvironmentAt(c.x, c.y) instanceof Plain);
  }


}
