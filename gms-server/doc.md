# 说明

## MonsterStatus 状态说明

`MonsterStatus` 不全是“怪物 Buff”，其中也包含玩家施加的减益、控制效果、免疫和反射状态。

| 状态 | 含义 | 典型效果 |
|---|---|---|
| `WATK` | 物理攻击提升 | 增加怪物物理攻击力 |
| `WDEF` | 物理防御提升 | 增加怪物物理防御力 |
| `NEUTRALISE` | 消除增益/中和 | 通常用于抵消或解除怪物的防御类状态 |
| `PHANTOM_IMPRINT` | 幻影印记 | 幻影相关的特殊标记，代码注释标记为待测试 |
| `MATK` | 魔法攻击提升 | 增加怪物魔法攻击力 |
| `MDEF` | 魔法防御提升 | 增加怪物魔法防御力 |
| `ACC` | 命中率提升 | 提高怪物命中率 |
| `AVOID` | 回避率提升 | 提高怪物回避率 |
| `SPEED` | 移动速度提升 | 增加怪物移动速度 |
| `STUN` | 晕眩 | 怪物短时间无法行动 |
| `FREEZE` | 冻结 | 怪物无法移动或行动 |
| `POISON` | 中毒 | 持续造成伤害 |
| `SEAL` | 封印 | 限制怪物使用技能或特殊能力 |
| `SHOWDOWN` | 挑衅/对决标记 | 通常提高怪物受到的伤害，并可能影响掉落或经验 |
| `WEAPON_ATTACK_UP` | 物理攻击强化 | 怪物物理攻击力上升 |
| `WEAPON_DEFENSE_UP` | 物理防御强化 | 怪物物理防御力上升 |
| `MAGIC_ATTACK_UP` | 魔法攻击强化 | 怪物魔法攻击力上升 |
| `MAGIC_DEFENSE_UP` | 魔法防御强化 | 怪物魔法防御力上升 |
| `DOOM` | 毁灭/变形 | 通常使怪物进入被削弱或特殊变形状态 |
| `SHADOW_WEB` | 影网 | 限制怪物移动，并可能造成持续伤害 |
| `WEAPON_IMMUNITY` | 物理免疫 | 免疫或大幅降低物理攻击伤害 |
| `MAGIC_IMMUNITY` | 魔法免疫 | 免疫或大幅降低魔法攻击伤害 |
| `HARD_SKIN` | 硬皮 | 降低受到的伤害 |
| `NINJA_AMBUSH` | 忍者伏击 | 持续性的特殊伤害效果 |
| `ELEMENTAL_ATTRIBUTE` | 元素属性 | 表示怪物的元素抗性、弱点或属性状态 |
| `VENOMOUS_WEAPON` | 毒性武器 | 怪物攻击附带中毒或毒属性效果 |
| `BLIND` | 失明 | 降低怪物命中率或攻击准确度 |
| `SEAL_SKILL` | 技能封印 | 禁止怪物使用技能 |
| `INERTMOB` | 怪物静止 | 怪物无法移动或暂时失去行动能力 |
| `WEAPON_REFLECT` | 物理反射 | 反弹玩家造成的物理伤害 |
| `MAGIC_REFLECT` | 魔法反射 | 反弹玩家造成的魔法伤害 |

### 补充说明

- `WDEF` 和 `NEUTRALISE` 使用相同的掩码 `0x2`。
- `MATK` 和 `PHANTOM_IMPRINT` 使用相同的掩码 `0x4`。
- `WEAPON_REFLECT`、`MAGIC_REFLECT`、`NEUTRALISE`、`PHANTOM_IMPRINT` 的第二个参数为 `true`，表示它们在封包状态处理中具有特殊的优先顺序。
- `WEAPON_ATTACK_UP` 与 `WATK`、`MAGIC_ATTACK_UP` 与 `MATK` 虽然含义相近，但通常对应不同的客户端显示或技能机制。

## 职业名称对照

| 中文名 | 枚举名 |
|---|---|
| 新手 | `BEGINNER` |
| 战士 | `WARRIOR` |
| 剑客 | `FIGHTER` |
| 勇士 | `CRUSADER` |
| 英雄 | `HERO` |
| 准骑士 | `PAGE` |
| 骑士 | `WHITEKNIGHT` |
| 圣骑士 | `PALADIN` |
| 枪战士 | `SPEARMAN` |
| 龙骑士 | `DRAGONKNIGHT` |
| 黑骑士 | `DARKKNIGHT` |
| 魔法师 | `MAGICIAN` |
| 法师（火毒） | `FP_WIZARD` |
| 巫师（火毒） | `FP_MAGE` |
| 魔导师（火毒） | `FP_ARCHMAGE` |
| 法师（冰雷） | `IL_WIZARD` |
| 巫师（冰雷） | `IL_MAGE` |
| 魔导师（冰雷） | `IL_ARCHMAGE` |
| 牧师 | `CLERIC` |
| 祭司 | `PRIEST` |
| 主教 | `BISHOP` |
| 弓箭手 | `BOWMAN` |
| 猎人 | `HUNTER` |
| 射手 | `RANGER` |
| 神射手 | `BOWMASTER` |
| 弩弓手 | `CROSSBOWMAN` |
| 游侠 | `SNIPER` |
| 箭神 | `MARKSMAN` |
| 飞侠 | `THIEF` |
| 刺客 | `ASSASSIN` |
| 无影人 | `HERMIT` |
| 隐士 | `NIGHTLORD` |
| 侠客 | `BANDIT` |
| 独行客 | `CHIEFBANDIT` |
| 侠盗 | `SHADOWER` |
| 海盗 | `PIRATE` |
| 拳手 | `BRAWLER` |
| 斗士 | `MARAUDER` |
| 冲锋队长 | `BUCCANEER` |
| 火枪手 | `GUNSLINGER` |
| 大副 | `OUTLAW` |
| 船长 | `CORSAIR` |
| 巡查员 | `MAPLELEAF_BRIGADIER` |
| 管理员 | `GM` |
| 超级管理员 | `SUPERGM` |
| 初心者 | `NOBLESSE` |
| 魂骑士(1转) | `DAWNWARRIOR1` |
| 魂骑士(2转) | `DAWNWARRIOR2` |
| 魂骑士(3转) | `DAWNWARRIOR3` |
| 魂骑士(4转) | `DAWNWARRIOR4` |
| 炎术士(1转) | `BLAZEWIZARD1` |
| 炎术士(2转) | `BLAZEWIZARD2` |
| 炎术士(3转) | `BLAZEWIZARD3` |
| 炎术士(4转) | `BLAZEWIZARD4` |
| 风灵使者(1转) | `WINDARCHER1` |
| 风灵使者(2转) | `WINDARCHER2` |
| 风灵使者(3转) | `WINDARCHER3` |
| 风灵使者(4转) | `WINDARCHER4` |
| 夜行者(1转) | `NIGHTWALKER1` |
| 夜行者(2转) | `NIGHTWALKER2` |
| 夜行者(3转) | `NIGHTWALKER3` |
| 夜行者(4转) | `NIGHTWALKER4` |
| 奇袭者(1转) | `THUNDERBREAKER1` |
| 奇袭者(2转) | `THUNDERBREAKER2` |
| 奇袭者(3转) | `THUNDERBREAKER3` |
| 奇袭者(4转) | `THUNDERBREAKER4` |
| 战童 | `LEGEND` |
| 龙初心 | `EVAN` |
| 战神(1转) | `ARAN1` |
| 战神(2转) | `ARAN2` |
| 战神(3转) | `ARAN3` |
| 战神(4转) | `ARAN4` |
| 龙神1 | `EVAN1` |
| 龙神2 | `EVAN2` |
| 龙神3 | `EVAN3` |
| 龙神4 | `EVAN4` |
| 龙神5 | `EVAN5` |
| 龙神6 | `EVAN6` |
| 龙神7 | `EVAN7` |
| 龙神8 | `EVAN8` |
| 龙神9 | `EVAN9` |
| 龙神10 | `EVAN10` |