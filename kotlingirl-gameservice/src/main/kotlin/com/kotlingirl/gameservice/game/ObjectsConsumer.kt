package com.kotlingirl.gameservice.game

import com.kotlingirl.gameservice.communication.PawnDto

class ObjectsConsumer(private val mechanics: Mechanics ) {

    fun consume(objects: MutableList<Any>, elapsed: Long) {
        consumeBombs(objects,elapsed)
        consumePawns(objects)
        consumeFires(objects, elapsed)
        consumeBonuses(objects)
    }


    private fun consumeBombs(objects: MutableList<Any>, elapsed: Long) {
        mechanics.bombs.forEach {
            if (it.timeLeft == 0) objects.addAll(mechanics.explose(it))
            else {
                objects.add(it.dto)
                it.tick(elapsed)
            }
        }
        mechanics.bombs.removeIf { it.explosed }
    }

    private fun consumePawns(objects: MutableList<Any>) {
        val objPawns = objects.filter { it is PawnDto }.map { it as PawnDto }
        mechanics.pawns.values.forEach {
            if (!it.alive) {
                if (objPawns.contains(it.dto)) {
                    objects.remove(it.dto); objects.add(it.dto)
                }
            }
        }
        mechanics.pawns.values.forEach {
            if (!objPawns.contains(it.dto)) {
                it.direction = ""; objects.add(it.dto)
            }
        }
    }

    private fun consumeFires(objects: MutableList<Any>, elapsed: Long) {
        mechanics.fires.removeIf { it.leftTime == 0 }
        mechanics.fires.forEach { objects.add(it.dto); it.tick(elapsed) }
    }

    private fun consumeBonuses(objects: MutableList<Any>) {
        mechanics.bonuses.forEach { if(it.taken) objects.add(it) }
        mechanics.bonuses.removeIf { it.taken }
    }
}