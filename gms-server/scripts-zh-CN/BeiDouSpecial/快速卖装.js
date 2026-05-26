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

    if (status === 0) {
        var itemId = cm.getInventoryEquip(1).getItemId();
        let text = "你要将背包 25 格之后的装备全部卖掉吗？\r\n\r\n";
        text += "#L0#我再考虑考虑#l\r\n\r\n";
        text += "#L1#卖！#l\r\n";
        cm.sendSimple(text);
    } else if (status === 1) {
        if (selection === 0) {
            cm.sendOk("考虑好了再来找我！");
            cm.dispose();
        } else if (selection === 1) {
            cm.sellAllEquipsFromPosition(25);
            cm.sendOk("卖完了！");
            cm.dispose();
        } else {
            cm.dispose();
        }
    } else {
        cm.dispose();
    }
}