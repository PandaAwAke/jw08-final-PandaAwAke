# Mandas Java Tiled2D Engine & GourdGame

Made by PandaAwAke, Nanjing University, November 2021.

## Preface

本仓库包含：

* 一个非常**简易**的 2D Tile Map 游戏引擎 (com.mandas.tiled2d)；
* 用它制作的葫芦娃泡泡堂游戏 (com.pandaawake.gourdgame)。

起因是《Java高级程序设计》课程的作业5，根据游戏编程需求干脆写个引擎帮忙罢了。



## Mandas Java Tiled2D Engine

### 简介

该引擎不依赖于您的游戏设计。

结构组成（下述被 * 标记的类不是引擎运行必须的，没说明的类说明没写好（x））：

* 包 com.mandas.tiled2d.core
  * Application：全局类，会更新整个游戏；
  * GameApplication：一个接口，在使用 Mandas 之前必须定义一个类实现它，描述游戏逻辑；
  * MainWindow：窗口类，它创建一个窗口，可以自定义标题、大小等；
* 包 com.mandas.tiled2d.renderer
  * Renderer：渲染器，基于 JPanel、Graphics (最早借用并改写了 AsciiPanel，后来重写了一个版本)，使用它来在窗口上画出游戏画面
  * Texture：材质，包装一个 BufferedImage，提供缩放等功能
  * Camera：相机，可以进行移动、缩放
  * TileTextures *：描述了一组有顺序的 Texture，可以作为游戏中某个可绘制对象的成员存储材质表示
* 包 com.mandas.tiled2d.scene
  * 描述了简单的 ECS (Entity Component System)
  * Component：Entity 的基本组件，包含一些基本功能
    * CameraComponent：包含一个相机，有该组件的 Entity 有能力操纵全局相机
    * TileTextureRenderComponent：包含一些渲染的材质设定，有该组件的 Entity 将被Renderer 自动渲染
    * TransformComponent：包含一些变换，目前仅支持移动，可以与 TileTextureRenderComponent 组合达到想要的渲染效果
  * Entity：由一堆不同类型的 Component 组成，可以用于表示我们游戏中的精灵
  * Scene：包含一堆 Entity，它们会被自动更新
* 包 com.mandas.tiled2d.utils
  * Pair<A, B>：描述二元对（内容可变）
  * IntPair：二元整数对，含 compareTo 方法
  * FloatPair：二元浮点数对，含 compareTo 方法
* TileFileParser *：解析一个 Tiled 图片，获取某个位置的 Texture。如下例所示
    ![TiledMap](resources/tile_example.png)
* com.mandas.tiled2d.Config
  * 描述引擎的各项数据设置，比如版本号，帧率等

如上所述，Mandas 包含了引擎的基本功能，比如创建绘制环境并不断更新游戏等；但有太多功能暂未实现。而且真正的游戏引擎基于 OpenGL, Vulkan, Direct3D 等 Graphics API 实现跨平台与高效绘制，但我仅仅使用了 Graphics。以下是一些未来可能会加入的机制（取决于我是否有兴趣继续开发以及是否有开发的需要）

* Event 机制，用于捕获并传递各种键盘鼠标事件 (暂时没有必要)；
* 音效系统 (有点小难)；
* ~~矩阵变换操作~~ (半实现)；
* ~~Camera 与更大的场景显示~~ (已实现)；
* ~~ECS~~ (已实现)；
* ……



### 快速上手

想要使用 Mandas Java Tiled2D Engine？跟着这个例子一起完成简单的上手吧！

1. 将 com.mandas.tiled2d 导入您的 java 项目目录中，并修改 Config 类的参数，以满足您的需求；

2. 创建您的游戏项目代码包，假定包名为 com.example.game；

3. 创建您的游戏主逻辑类，要实现接口 com.mandas.tiled2d.core.GameApplication，如下：

   ```java
   package com.example.game;
   
   import com.mandas.tiled2d.core.GameApplication;
   
   public class ExampleGameApp implements GameApplication {
       
       ...
       
       public ExampleGameApp() {
           ...
       }
       
       @Override
       public void InitRenderer() {
           Renderer.Init(...);
           ...
       }
       
       @Override
       public void OnRender() {
           ...
       }
       
       @Override
       public void OnUpdate(float timestep) {
           ...
       }
       
       @Override
       public void OnKeyPressed(KeyEvent e) {
           
       }
       
       ...
   }
   ```
   

需要解释的是，在您完成 Mandas 引擎的 Config 中的帧率设置后，在每一帧绘制前会调用您上面代码中的 OnUpdate 方法，然后调用您上面代码中的 OnRender 方法。

* InitRenderer()：稍后再说；
   * OnRender()：在这个函数中，使用 Mandas 的 Renderer 对象完成绘制相关的操作（稍后见）；
   * OnUpdate(float timestep)：在这个函数中，完成您游戏随时间流逝发生的变化操作。timestep 是距离上次 OnUpdate 调用过了多少**秒** (精确到千分位，也就是毫秒级别)；
   * OnKeyPressed(KeyEvent e)：捕捉按键事件，由于 Mandas 暂时没有 Event 系统，所以将就一下（摆烂）
   
4. 创建一个 mandas.tiled2d.scene.Scene 或其子类的对象，这是您游戏的基本场景；
   
5. 创建一个 mandas.tiled2d.renderer.Camera 对象，作为默认相机，设置**显示区域**的大小；

6. 进行 Renderer 相关初始化 InitRenderer()
   您必须在这个函数中调用 Renderer.Init(...)，指定如下内容：

   - 游戏地图的宽度和高度分别是多少个 Tile；
   - 一个 Tile 的宽度和高度分别是多少像素；
   - 计分板的宽度（计分板为游戏显示区域右侧多出来的部分），如果不需要您可以设置成0；
   - 缺省默认材质/颜色 (emptyTexture)，即如果地图中有某些位置没有设定显示什么内容，就显示这个 Texture 或者这个颜色；
   - Scene 的信息。Scene 中的 Entity 若包含绘制相关的 Component，则会被自动绘制；
   - 刚刚创建的 Camera，这是游戏的最基本相机，而且游戏的显示区域大小也将由该相机实现初始化。
   - 举个例子，比如游戏地图是20x20 个 Tile，显示区域是15x15 个 Tile，那么您在此处提供的 Camera 将是 15x15 大小的，在 Renderer.Init 参数中将提供 20x20。而且游戏的窗口创建后显示的区域就是 15x15 的，但游戏地图实际为 20x20，不会完全显示（除非您修改相机的位置和缩放尺寸）。

7. 创建 main 函数入口
   下面是一个标准的 main 函数入口，按理来说您不需要在函数体内进行其他操作。

   ```java
   package com.example.game;
   
   import com.mandas.tiled2d.core.Application;
   
   public class Main {
   
       public static void main(String[] args) {
           ExampleGameApp gameApp = new ExampleGameApp();
           Application application = new Application(gameApp, "窗口标题");
   
           application.createWindowAndRun();
       }
   
   }
   ```

   当然，您可以使用其他方式获取您的 gameApp，并用它创建 Application 单例。



OK！这就成功了。



### Renderer

| 方法名                          | 描述               |
| ------------------------------- | ------------------ |
| 静态方法 Renderer.getRenderer() | 获取 Renderer 单例 |

Renderer 共有四种绘制类型：固定 Tile 绘制，Scene 绘制，浮空 Tile 绘制，Scoreboard 绘制

* 固定 Tile 绘制

  Renderer 的*固定 Tile 绘制* 面向 Tiled 2D Game 地图，一切操作基于显示区域中的 Tile。比如，地图可以是 15*15 的 Tiles 组成的，每个 Tile 的宽度和高度可以是 32。

  坐标 (0, 0) 代表**左上角**的那个 Tile，(x, y) 中，x 代表横坐标，y 代表纵坐标。

  为了节省开销，只要地图某一点的材质没有发生改变，那么它就不会被重画。

  | 方法名                                                       | 描述                                                         |
  | ------------------------------------------------------------ | ------------------------------------------------------------ |
  | 静态方法 Renderer.getRenderer()                              | 获取 Renderer 单例                                           |
  | clear()                                                      | 将整个显示地图区域设置成 emptyTexture                        |
  | setTexture(Texture texture, int x, int y)                    | 设置地图位置 (x, y) 显示的材质为 texture                     |
  | addRepaintTilePositions(Collection<IntPair> repaintPositions) | repaintPositions 中的所有位置都将在下一次重画（仅重画一次，如还需重画则需重新添加） |

* Scene 绘制
  

说白了就是会把 Scene 中的 Entity 检查一遍，若某个 Entity 包含绘制相关的 Component，就根据 Component 的信息把它自动绘制了。绘制方法和下面的浮空 Tile 绘制差不多，只不过您什么也不用做。

* 浮空 Tile 绘制
  

这种绘制方式面向您游戏中的 Sprites，当然 Sprites 的显示也是基于 Tile 的。

| 方法名                                                       | 描述                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| addFloatingTile(FloatPair position, Texture texture, boolean repaintNearTiles) | 添加浮空 Tile 绘制，支持自由的浮点数位置 (位置的单位仍然是一个 Tile，比如(0.5, 0))，需要指定是否需要重画周围的固定 Tile (默认重画)。<br />需要注意的是这个函数只会绘制一次，如需一直绘制则需在每次绘制前调用它。 |

其实上面固定 Tile 绘制的 addRepaintTilePositions() 方法很好用，~~如果您在游戏中移除了一个 Sprite，那么需要重画它周围的固定 Tile，否则可能会残留 Sprite 的图像~~（现在不需要了！）。

* Scoreboard 绘制
  

这种绘制方式面向游戏右侧的计分板。如果您指定了计分板的宽度大于0的话。

| 方法名                                                       | 描述                               |
| ------------------------------------------------------------ | ---------------------------------- |
| clearScoreboard()                                            | 清空计分板                         |
| drawScoreboardString(int startX, int startY, String string, Font font) | 在计分板指定位置写字，可以指定字体 |
| drawScoreboardTile(int startX, int startY, Texture texture)  | 在计分板指定位置画材质             |



### ECS (scene)

您可以自定义您游戏的 Scene，但需要继承 mandas.tiled2d.scene.Scene 以获得引擎的支持。

您的每一个精灵都可以继承 Entity 类，例如：

```java
public class Sprite extends Entity {
    public Sprite(...) {

        ...

        // Components
        this.addComponent(new TileTextureRenderComponent());
        this.addComponent(new TransformComponent());
    }
}
```

然后使用 Entity 类的 addComponent 去添加引擎中自带的组件。

目前引擎中自带的组件如下：

| Component 名               | 描述                                                         |
| -------------------------- | ------------------------------------------------------------ |
| CameraComponent            | 使这个 Entity 可以操作一个自己的相机，可以通过设置该组件来达到切换游戏显示相机的目的 |
| TileTextureRenderComponent | 描述这个 Entity 该如何绘制，包括由多少个 Tile 组成，每个 Tile 分别在该 Entity 的哪个**相对位置**。比如你的人物大小是 1x2 (2个 Tile 高)，那么可以分别指定 (0, 0) 和 (0, 1) 两个 Tile 位置分别用什么 Texture 去绘制。<br />**拥有该组件的 Entity 将被自动绘制**。 |
| TransformComponent         | 描述这个 Entity 绘制时的变换，目前只支持平移。<br />translationX 和 translationY 都是以 Tile 为单位的，比如描述某个精灵在地图的第 (1.5, 2.4) 个 Tile 处；而不是像图形 API 一样以屏幕为单位。 |

下面是一些使用例子。

```java
public class Calabash extends Entity {
    
    public Calabash() {
        // 渲染组件
        addComponent(new TileTextureRenderComponent());
        // 变换组件
        addComponent(new TransformComponent());
        // 相机组件，true 代表用这个相机作为游戏显示的主相机
        addComponent(new CameraComponent(new Camera(15, 15), true));
        
        // 设置该 Entity 渲染时在 (0,0) 处用什么图案
        getTileTextureRenderComponent().addPositionAndTexture(new FloatPair(0, 0), ...);
    }
    
    public void setX(float x) {
        
        ...
            
        getTransformComponent().setTranslationX(x);				// 改变精灵渲染的位置
        getCameraComponent().getCamera().setTranslationX(...);	// 改变相机的显示位置
    }
}
```

最后，如果您定义了您自己的 Scene 类 (继承mandas.tiled2d.scene.Scene)，而且实现了 OnUpdate 方法的话，别忘记调用 super.OnUpdate。否则，引擎可能不会自动处理这些 Entity。

```java
public class Scene extends com.mandas.tiled2d.scene.Scene {
    
	...
    
    @Override
    public void OnUpdate(float timestep) {
        super.OnUpdate(timestep);
        
        ...
        
    }
    
	...
}
```

以及，如果您创建了 Entity 对象（比如上面的 Calabash）并希望引擎帮忙自动处理这些 Entity，请用 Scene 中的 addEntity 或 setEntities 向引擎添加这些 Entity。您添加的或设置的 Entities 是什么顺序，就将按照什么顺序一一渲染它们。





### 其他

引擎中还有不少辅助函数/辅助类可供使用，GourdGame 基本全面地使用了引擎的一切，可供参考。