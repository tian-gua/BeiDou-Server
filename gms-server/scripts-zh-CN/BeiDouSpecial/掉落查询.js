var status;

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
        var text = '当前地图#b#e存活#k#n怪物列表一览，点击可查看掉落列表：\r\n'; //怪物选择展示消息
        var pairs = cm.currentMapMonster();
        for (var i = 0; i < pairs.length; i++) {
            text += `#L${paris[i].getRight()}# ${pairs[i].getLeft()}\r\n`;
        }
        cm.sendSimple(text);
    } else if (status === 1) {
        var monsterId = selection;
        var dropList = cm.getDropList(monsterId);
        var text = `怪物 ${monsterId} 的掉落列表：\r\n`;
        for (var i = 0; i < dropList.length; i++) {
            text += `#i${dropList[i].getRight()}# - ${dropList[i].getLeft()}\r\n`;
        }
        cm.sendOk(text);
        cm.dispose();
    } else {
        cm.dispose();
    }
}