var status;
var monsterPair = [];
var Pair;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        status--;
    }

    if (status === 0) {
        Pair = Java.type('org.gms.util.Pair');
        var text = '当前地图#b#e存活#k#n怪物列表一览，点击可查看掉落列表：\r\n'; //怪物选择展示消息
        monsterPair = cm.currentMapMonster();
        if (monsterPair.length == 0) {
            cm.sendOk("当前地图没有怪物。");
            cm.dispose();
            return;
        }

        for (var i = 0; i < monsterPair.length; i++) {
            text += `#L${i}# ${monsterPair[i].getLeft()}\r\n`;
        }
        cm.sendSimple(text);
    } else if (status === 1) {
        var monsterId = monsterPair[selection].getRight();
        var monsterName = monsterPair[selection].getLeft();
        var dropList = cm.getDropList(monsterId);
        var text = `怪物 ${monsterName} 的掉落列表：\r\n`;
        for (var i = 0; i < dropList.length; i++) {
            if (monsterName == null || monsterName == "null" || monsterId == null) {
                continue;
            }
            text += `#i${dropList[i].getRight()}# - ${dropList[i].getLeft()}\r\n`;
        }
        cm.sendOk(text);
        cm.dispose();
    } else {
        cm.dispose();
    }
}