package com.teamtwo.aerolites.Entities;

import com.teamtwo.engine.Utilities.MathUtil;

/**
 * @author Matthew Threlfall
 */
public class ScoreObject {
    private int score;
    private int scoreTotal;
    private int asteroidsDestroyed;
    private int asteroidsDestroyedTotal;
    private int enemiesDestoryed;
    private int enemiesDestoryedTotal;
    private float timeAlive;
    private float timeAliveTotal;
    private int bulletsFired;
    private int bulletsFiredTotal;
    private int bulletsMissed;
    private int bulletsMissedTotal;
    private float timeSpentBoosting;
    private float timeSpentBoostingTotal;

    public ScoreObject(){
        score = 0;
        scoreTotal = 0;
        asteroidsDestroyed = 0;
        asteroidsDestroyedTotal = 0;
        enemiesDestoryed = 0;
        enemiesDestoryedTotal = 0;
        timeAlive = 0;
        timeAliveTotal = 0;
        bulletsFired = 0;
        bulletsFiredTotal = 0;
        bulletsMissed = 0;
        bulletsMissedTotal = 0;
        timeSpentBoosting = 0;
        timeSpentBoostingTotal = 0;
    }

    public void newLevel(){
        score = 0;
        asteroidsDestroyed = 0;
        enemiesDestoryed = 0;
        timeAlive = 0;
        bulletsFired = 0;
        bulletsMissed = 0;
        timeSpentBoosting = 0;
    }

    public void incrementAsteroidsDestoryed(){
        asteroidsDestroyed++;
        asteroidsDestroyedTotal++;
        score+=50;
        scoreTotal+=50;
    }
    public void incrementEnemiesKilled(){
        enemiesDestoryed++;
        enemiesDestoryedTotal++;
        score+=80;
        scoreTotal+=80;
    }
    public void incrementTimeAlive(float dt){
        timeAlive+=dt;
        timeAliveTotal+=dt;
    }
    public void incrementTimeBoosting(float dt){
        timeSpentBoosting+=dt;
        timeSpentBoostingTotal+=dt;
    }
    public void incrementBulletsFired(){
        bulletsFired++;
        bulletsFiredTotal++;
    }
    public void incrementBulletsMissed(){
        bulletsMissed++;
        bulletsMissedTotal++;
    }

    public float getAccuracy(){
        float accuracy = 0;
        if(bulletsFired>0) {
            accuracy = MathUtil.round(((bulletsFired-bulletsMissed) / bulletsFired) * 100, 2);
        }
        return accuracy;
    }
    public float getTotalAccuracy(){
        float accuracy = 0;
        if(bulletsFiredTotal>0) {
            accuracy = MathUtil.round(((bulletsFiredTotal-bulletsMissedTotal) / bulletsFiredTotal) * 100, 2);
        }
        return accuracy;
    }
    public int getScore(){
        return score;
    }
    public int getScoreTotal(){
        return scoreTotal;
    }
    public void roundValues(){
        timeAlive = MathUtil.round(timeAlive,3);
        timeAliveTotal = MathUtil.round(timeAliveTotal,3);
        timeSpentBoosting = MathUtil.round(timeSpentBoosting,3);
        timeSpentBoostingTotal = MathUtil.round(timeSpentBoostingTotal,3);
    }

    public int getAsteroidsDestroyed() {
        return asteroidsDestroyed;
    }

    public int getAsteroidsDestroyedTotal() {
        return asteroidsDestroyedTotal;
    }

    public int getEnemiesDestoryed() {
        return enemiesDestoryed;
    }

    public int getEnemiesDestoryedTotal() {
        return enemiesDestoryedTotal;
    }

    public float getTimeAlive() {
        return timeAlive;
    }

    public float getTimeAliveTotal() {
        return timeAliveTotal;
    }

    public int getBulletsFired() {
        return bulletsFired;
    }

    public int getBulletsFiredTotal() {
        return bulletsFiredTotal;
    }

    public int getBulletsMissed() {
        return bulletsMissed;
    }

    public int getBulletsMissedTotal() {
        return bulletsMissedTotal;
    }

    public float getTimeSpentBoosting() {
        return timeSpentBoosting;
    }

    public float getTimeSpentBoostingTotal() {
        return timeSpentBoostingTotal;
    }
}
