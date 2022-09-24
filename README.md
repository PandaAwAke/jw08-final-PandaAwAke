# 弹弹堂游戏

## 1.1 Gamerule
* 该游戏最多可支持4个人类玩家，并自带4个电脑玩家；
* 每个人类玩家拥有3条命，表现形式为“葫芦娃”，可以安放并提前引爆炸弹；
* 每个电脑玩家拥有2条命，表现形式为“蛇精”，蛇精安放的炸弹不会伤害蛇精；
* 每个炸弹拥有3x3的爆炸范围（可穿墙），自动引爆时间为3秒；
* 人类全部消灭电脑玩家或电脑全部消灭人类玩家，则游戏结束。


## 1.2 Workflow
本游戏的操作流程如下：
1. 首先打开一个服务端 (要求com.pandaawake.gourdgame.Config中设定ServerMode=true)，该服
务端同时也搭载了一个客户端；
2. (可选) 接着打开不超过3个其他客户端 (要求com.pandaawake.gourdgame.Config中设定ServerMode=false)；
3. 在服务端窗口按F1开始游戏；
4. 每个窗口中可以按WASD向四个方向移动，按J键安放炸弹，按空格键提前引爆；
5. 游戏结束时，服务端将向所有客户端通知，并停止游戏。


## 1.3 Feature
游戏特征：
* 本游戏的地图详细内容由服务端决定，客户端不从本地加载地图而是接受服务端信息；
* 游戏信息在所有客户端完全同步；
* 每名玩家可以通过移动来移动相机，从而获得更好的地图视角，即地图并不只限于窗口大小。
