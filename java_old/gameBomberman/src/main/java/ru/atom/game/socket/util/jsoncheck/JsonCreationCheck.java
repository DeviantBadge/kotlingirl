package ru.atom.game.socket.util.jsoncheck;

import ru.atom.game.enums.MessageType;
import ru.atom.game.gamesession.properties.GameSessionPropertiesCreator;
import ru.atom.game.socket.message.response.OutgoingMessage;
import ru.atom.game.socket.message.response.messagedata.Replica;
import ru.atom.game.objects.base.interfaces.Replicable;
import ru.atom.game.objects.base.util.Point;
import ru.atom.game.objects.ingame.*;
import ru.atom.game.gamesession.properties.GameSessionProperties;
import ru.atom.game.socket.util.JsonHelper;

import java.util.List;

public class JsonCreationCheck {

    // made this class to check, am i correctly creating Json or not
    public static void main(String[] args) {
        GameSessionProperties properties = new GameSessionPropertiesCreator().createProperties();
        ObjectCreator creator = new ObjectCreator(properties);
        Point point = new Point(10,20);
        Pawn pawn = creator.createPawn(point);
        Bomb bomb = creator.createBomb(point, pawn);
        Bonus bonus1 = creator.createBonus(point, Bonus.BonusType.BOMBS);
        Bonus bonus2 = creator.createBonus(point, Bonus.BonusType.SPEED);
        Bonus bonus3 = creator.createBonus(point, Bonus.BonusType.RANGE);
        Fire fire = creator.createFire(point);
        Wall wall = creator.createWall(point);
        Wood wood = creator.createWood(point);

        Replica replica = new Replica();
        replica.addToReplica(pawn);
        replica.addToReplica(bomb);
        replica.addToReplica(bonus1);
        replica.addToReplica(bonus2);
        replica.addToReplica(bonus3);
        replica.addToReplica(fire);
        replica.addToReplica(wall);
        replica.addToReplica(wood);

        List<Replicable> replicables = replica.getData();
        replica.addAllToReplica(replicables);

        System.out.println(JsonHelper.toJson(point));
        System.out.println(JsonHelper.toJson(pawn));
        System.out.println(JsonHelper.toJson(bomb));
        System.out.println(JsonHelper.toJson(bonus1));
        System.out.println(JsonHelper.toJson(bonus2));
        System.out.println(JsonHelper.toJson(bonus3));
        System.out.println(JsonHelper.toJson(fire));
        System.out.println(JsonHelper.toJson(wall));
        System.out.println(JsonHelper.toJson(wood));

        OutgoingMessage message = new OutgoingMessage(MessageType.REPLICA, replica.toString());
        System.out.println(JsonHelper.toJson(message));
    }
}
