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

    // #装备升星#
    if (status === 0) {
        var itemId = cm.getInventoryEquip(1).getItemId();
        let text = "你要为 #r#i" + itemId + "##k升星吗？\r\n 每件装备最多能升 30星，升星费用为一些金币 和 1个#i4001126#\r\n\r\n";
        text += "#L0#我再考虑考虑#l\r\n\r\n";
        text += "#L1#普通升星#l\r\n\r\n";
        text += "#L2#祝福升星（#i2340000#）#l\r\n";
        cm.sendSimple(text);
    } else if (status === 1) {
        if (selection === 0) {
            cm.sendOk("考虑好了再来找我！");
            cm.dispose();
        } else if (selection === 1) {
            cm.enhanceEquip(30, false);
            cm.dispose();
        } else if (selection === 2) {
            cm.enhanceEquip(30, true);
            cm.dispose();
        } else {
            cm.dispose();
        }
    } else {
        cm.dispose();
    }
}