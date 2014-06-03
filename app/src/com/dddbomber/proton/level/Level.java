package com.dddbomber.proton.level;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import com.dddbomber.proton.assets.Screen;
import com.dddbomber.proton.entity.Colors;
import com.dddbomber.proton.entity.Enemy;
import com.dddbomber.proton.entity.Entity;
import com.dddbomber.proton.entity.EntitySpawner;
import com.dddbomber.proton.entity.Player;
import com.dddbomber.proton.input.InputHandler;
import com.dddbomber.proton.menu.ColorSelectMenu;
import com.dddbomber.proton.menu.GameMenu;
import com.dddbomber.proton.menu.Menu;

public class Level {
	
	public int shotsFired, shotsHit;
	
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	
	Random random = new Random();
	
	public Player player;
	
	public int won;//1 - win // 2 - lose // 3 - draw
	
	public int enemies;
	
	public int countdown = 220;
	
	public Level(Colors color){
		player = new Player(color, 80, 160);
		entities.add(new EntitySpawner(player));
		entities.add(new EntitySpawner(new Enemy(400, 160)));
		slowDown = 30;
	}
	
	public int xScroll, yScroll;
	public int ticks = 0;
	
	public void render(Screen screen){
		if(countdown < 61 && countdown > 0){
			screen.draw("GO!", screen.width/2-12, 64, 0xffffff, 2);
		}else if(countdown >= 60){
			screen.draw(""+(countdown)/60, screen.width/2-6, 96, 0xffffff, 2);
		}

		if(player.removed || (enemies < 1 && countdown == 0)){
			if(restartDelay-- <= 0){
				String msg = "";
				if(won == 1)msg = "YOU WON";
				if(won == 2)msg = "YOU LOSE";
				if(won == 3)msg = "DRAW";
				screen.draw(msg, screen.width/2-msg.length()*6, 64, 0xffffff, 2);
				screen.draw("PRESS SPACE TO RESTART", screen.width/2-132, 96, 0xffffff, 2);
			}
		}
		if(slowDown > 0 && ticks % 2 == 0){
			//Skip a tick
		}else{
			screen.fill(0, 0, screen.width, screen.height, 0x2E2128, 20);
			for(Entity e : entities){
				e.render(this, screen, xScroll, yScroll);
			}
			
		}
		int offset = 8;
		for(Entity e : entities){
			if(e instanceof Enemy || e instanceof Player){
				renderOverlayForEntity(screen, offset, e);
				offset = screen.width-72;
			}
		}
	}
	
	public void renderOverlayForEntity(Screen screen, int offset, Entity player){
		screen.fill(offset, screen.height-32, 64, 24, player.colors.col, 10);

		screen.draw(""+player.health, offset, screen.height-31, player.colors.light, 2);
		screen.fill(offset, screen.height-16, 64, 8, player.colors.col);
		screen.fill(offset, screen.height-16, player.health*2, 8, player.colors.light);
	}
	
	public int slowDown = 0;
	
	private int restartDelay = 120;
	
	public void tick(InputHandler input){
		if(countdown > 0)countdown--;
		ticks++;
		slowDown--;
		if(slowDown > 0 && ticks % 2 == 0){
			return;
		}
		enemies = 0;
		for(int i = 0; i < entities.size(); i++){
			Entity e = entities.get(i);
			e.tick(this, input);
			if(e.removed){
				entities.remove(i--);
				if(e instanceof Player)Colors.unused.add(e.colors);
			}else{
				if(e instanceof Enemy || (e instanceof EntitySpawner && ((EntitySpawner)e).entityToSpawn instanceof Enemy))enemies++;
			}
		}
		if(player.removed || (enemies < 1 && countdown == 0)){
			if(player.removed){
				won = 2;
				if(enemies < 1)won = 3;
			}else{
				won = 1;
			}
			if(restartDelay-- <= 0){
				if(input.keyboard.keys[KeyEvent.VK_SPACE]){
					Menu.menu = new ColorSelectMenu();
				}
			}
		}
	}
}