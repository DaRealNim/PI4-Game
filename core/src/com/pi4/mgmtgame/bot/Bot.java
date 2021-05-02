package com.pi4.mgmtgame.bot;

import com.pi4.mgmtgame.Inventory;
import com.pi4.mgmtgame.Map;
import com.pi4.mgmtgame.networking.Server;
import com.pi4.mgmtgame.resources.Resources;
import com.pi4.mgmtgame.resources.Plant;
import com.pi4.mgmtgame.resources.Grain;
import com.pi4.mgmtgame.blocks.*;

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
  private ArrayList<Coord> goodFieldSpots;
  private Map map;
  private Inventory inv;
  private int botID;
  private Server server;

//Respect clean code rules and indentation I'm tired of the code looking like a slum
//if I see another "if" tree I'm going insane.

  public Bot(Map m, Inventory i, int id, Server sv)
  {
    map = m;
    inv = i;
    id = botID;
    server = sv;
    goodFieldSpots = scanForPlainsNearLakes();
  }

  public void play() //This is the most abstract function, so it is at the top
  {
    //start turn
    //harvest()
    sellResources();
    //buildStructures()
    //sabotage()
    //end
  }

  public void sellResources() //Second most abstract, so on.
  {
    for (Plant p : inv.getPlants())
    {
      if (isWorthSelling(p))
        server.sellPlant(p, p.getVolume());
    }
    for (Grain g : inv.getSeeds())
    {
      if (isWorthSelling(g))
        server.sellGrain(g, g.getVolume());
    }
  }

  public void buildStructures()
  {
  }

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
        if (!map.isNotLake(x + hor, y + vert)) //negative boolean functions suck dick fuck you
          return (true);
      }
    }
    return (false);
  }

  public boolean canBuyTerrain(Coord c) //I know this function also exists in Server but it's only for players since the server's invArray doesn't account for the inventory in this class. 
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

  public boolean isWorthSelling(Resources r)
  {
    if (r.getPrice() >= priceAverage(inv))
      return (true);
    return (false);
  }
}
