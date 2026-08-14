var status;
var firstSelection;

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

    // cm.getPlayer().message("mode=" + mode + ",type=" + type + ",selection=" + selection);
    // #装备开槽#
    if (status === 0) {
        var itemId = cm.getInventoryEquip(1).getItemId();
        let text = "你要为 #r#i" + itemId + "##k升星吗？\r\n 每个装备最多能升 30 个星级，每次升星费用为 10w 金币\r\n\r\n";
        text += "#L0#我再考虑考虑#l\r\n\r\n";
        text += "#L1#普通升星#l\r\n";
        text += "#L2#枫叶升星（保护券模式）#l\r\n";
        cm.sendSimple(text);
    } else if (status === 1) {
        if (selection === 0) {
            cm.sendOk("考虑好了再来找我！");
            cm.dispose();
        } else if (selection === 1) {
            if (cm.getMeso() >= 100000 && cm.itemQuantity(4001126) >= 1) {
                var result = cm.enhanceEquip();
                if (result) {
                    cm.gainMeso(-100000);
                    cm.gainItem(4001126, -1);
                    cm.dispose();
                } else {
                    cm.sendOk("升星失败，装备不符合升星条件");
                    cm.dispose();
                }
            } else {
                cm.sendOk("你没有10w金币或者#i4001126#");
                cm.dispose();
            }
        } else if (selection === 2) {
            if (cm.getMeso() >= 100000) {

            } else {
                cm.sendOk("你没有10w金币");
                cm.dispose();
            }
        } else {
            cm.dispose();
        }
    } else {
        cm.dispose();
    }
}