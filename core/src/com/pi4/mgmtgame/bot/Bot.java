package com.pi4.mgmtgame.bot;

import com.pi4.mgmtgame.Inventory;
import com.pi4.mgmtgame.Map;
import com.pi4.mgmtgame.networking.Server;
import com.pi4.mgmtgame.resources.Resources;
import com.pi4.mgmtgame.resources.Plant;
import com.pi4.mgmtgame.resources.Grain;

public class Bot {
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
  }

  public void play() //This is the most abstract function, so it is at the top
  {
    //start turn
    //harvest()
    sellResources(this.inv);
    //buildStructures()
    //sabotage()
    //end
  }

  public void sellResources(Inventory inv) //Second least abstract, so on.
  {
    for (Plant p : inv)
    {
      if (isWorthSelling(p))
        server.sellPlant(p, p.getVolume());
    }
    for (Grain g : inv)
    {
      if (isWorthSelling(g))
        server.sellPlant(g, g.getVolume());
    }
  }

  public boolean isWorthSelling(Resources r)
  {
    if (r.getPrice() >= priceAverage(inv))
      return (true);
    return (false);
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

    for (Grain g : inv.getGrain())
    {
      sumOfPrices += g.getPrice();
      ++resCount;
    }

    return (sumOfPrices / resCount);
  }

  public boolean isThereLakeNear(int x, int y)
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

}
