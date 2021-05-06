package com.pi4.mgmtgame.bot;

import com.pi4.mgmtgame.Inventory;
import com.pi4.mgmtgame.Map;
import com.pi4.mgmtgame.networking.Server;
import com.pi4.mgmtgame.resources.Resources;
import com.pi4.mgmtgame.resources.Plant;
import com.pi4.mgmtgame.resources.Grain;
import com.pi4.mgmtgame.blocks.*;
import com.pi4.mgmtgame.blocks.Field;

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
  }

  private ArrayList<Coord> ownedStructures;
  private ArrayList<Coord> ownedTerrains;
  private ArrayList<Coord> goodFieldSpots;
  private Map map;
  private Inventory inv;
  private int botID;
  private Server server;

//Respect clean code rules and indentation I'm tired of the code looking like a slum
//if I see another "if" tree I'm going insane.

  public Bot(Map m, Inventory i, int id, Server sv, int HQx, int HQy)
  {
    map = m;
    inv = i;
    id = botID;
    server = sv;
    goodFieldSpots = scanForPlainsNearLakes();
    ownedTerrains = new ArrayList<Coord>();
    ownedStructures = new ArrayList<Coord>();

    Coord HQ = new Coord(HQx, HQy);

    for (int x = -1; x < 1; x++)
    {
      for (int y = -1; y < 1; y++)
      {
        ownedStructures.add(new Coord(HQ.x + x, HQ.y + y));
        ownedTerrains.add(new Coord(HQ.x + x, HQ.y + y));
      }
    }
  }

  public void play() //This is the most abstract function, so it is at the top
  {
    //start turn
    harvestFields();
    sellResources();
    buyTerrains();
    buildStructures();
    //sabotage()
    //end
  }

  public void harvestFields() //Second most abstract, so on.
  {
    ArrayList<Coord> ownedFields = getOwnedFields();

    for (Coord c : ownedFields)
    {
        Plant harvested;
        Field currField = (Field) map.getStructAt(c.x, c.y);

        if (canHarvest(currField))
        {
          harvested = currField.harvest();
          harvested.addVolume(4);
          inv.addPlant(harvested.getId(), harvested.getVolume());
        }
    }
  }

  public void sellResources()
  {
    for (Plant p : inv.getPlants())
    {
      if (priceIsHigherThanMarketAverage(p))
        server.sellPlant(p, p.getVolume());
    }
    for (Grain g : inv.getSeeds())
    {
      if (priceIsHigherThanMarketAverage(g))
        server.sellGrain(g, g.getVolume());
    }
  }

  public void buyTerrains()
  {
    int initialFunds = inv.getMoney();

    while (inv.getMoney() > initialFunds / 3 && inv.getMoney() >= 500) //This condition will be changed to something more elegant when there are maintenance costs.
    {
      for (Coord c : goodFieldSpots)
      {
        buyTerrainAt(c);
      }
    }
  }

  public void buildStructures() //This function will be modified when new structures are implemented.
  {
      buildFields();
  }


  public void buildFields()
  {
    int initialFunds = inv.getMoney();

    while (inv.getMoney() > initialFunds && inv.getMoney() >= 300) //This condition will be changed to something more elegant when there are maintenance costs.
    {
      for (Coord c : ownedTerrains)
      {
        buildFieldAt(c);
      }
    }
  }

  public void buyTerrainAt(Coord c)
  {
    Environment terrain = map.getEnvironmentAt(c.x, c.y);
    int terrainCost = terrain.getPrice();

    if (canBuyTerrainAt(c))
    {
      inv.giveMoney(terrainCost);
      terrain.setOwnerID(botID);
      ownedTerrains.add(c);
    }
  }

  public void buildFieldAt(Coord c)
  {
    Environment terrain = map.getEnvironmentAt(c.x, c.y);
    Field newField = new Field(c.x, c.y);
    int fieldCost = newField.getConstructionCost();

    if (fieldCost < inv.getMoney() && map.getStructAt(c.x, c.y) == null)
    {
      inv.giveMoney(fieldCost);
      map.setStructAt(c.x, c.y, newField);
      newField.setOwnerID(botID);
      ownedStructures.add(c);
    }
  }

  //This function will be modified when trade routes are implemented
  public ArrayList<Coord> scanForPlainsNearLakes()
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

  public ArrayList<Coord> getOwnedFields()
  {
    ArrayList<Coord> ownedFields = new ArrayList();

    for (Coord c : ownedStructures)
    {
      Structure currStruct = (Field) map.getStructAt(c.x, c.y);
      if (structIsField(currStruct))
        ownedFields.add(c);
    }

    return (ownedFields);
  }

  public int totalFieldTerrainCost(ArrayList<Coord> spots)
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

  public int priceAverage(Inventory inv)
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

  public boolean thereIsLakeNear(int x, int y)
  {
    for (int hor = -1; hor <= 1; hor++)
    {
      for (int vert = -1; vert <= 1; vert++)
      {
        if (!map.isNotLake(x + hor, y + vert)) //would be more interesting to know when things ARE lakes instead of !lakes.
          return (true);
      }
    }
    return (false);
  }

  //I know this function also exists in Server but it's only for players since the server's invArray doesn't account for the inventory of bots.
  public boolean canBuyTerrainAt(Coord c)
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

  public boolean priceIsHigherThanMarketAverage(Resources r)
  {
    if (r.getPrice() >= priceAverage(inv))
      return (true);
    return (false);
  }

  public boolean structIsField(Structure structBlock)
  {
    return (structBlock instanceof Field && structBlock != null);
  }

  public boolean canHarvest(Field field)
  {
    return (field.hasSeedGrown());
  }
}
