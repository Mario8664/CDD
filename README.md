# 锄大地[release版apk在此](https://github.com/Mario8664/CDD/blob/master/app/release/app-release.apk)

>### 简单的说明

这个锄大地开发时长3周，完全开源。
其中用到了自定义SurfaceView，进而编写了一个简单的仿照Unity的2d游戏框架。但是，事件系统、碰撞检测之类的比较耗时的组件没有实现。事实上针对这个游戏也用不到。


>### 特别说明

* 1、游戏的渲染是靠SurfaceView上面的画笔功能做的，每绘制一个元素就要一次CPU的调用，可以说效率极低，在安卓的低端手机上性能很差，如果能实现批处理就更好了
* 2、游戏的AI基本上是靠着直觉写的计算方法，写完后就想了解一下神经网络，结果发现原来神经网络基本上也是靠权重和阈值进行划分的。只不过那种AI可以训练，我这种就是写死的权重计算和阈值。如果以后有机会，可以增加真正的人工智能算法。
* 3、游戏中用到的安卓开发知识很少，因为前期写定了SurfaceView的基本框架后，后面只要自己写游戏引擎的框架就可以了，后期基本没有碰安卓部分的东西。

>### 来几张截图

![开始界面](https://github.com/Mario8664/CDD/blob/master/screenshot/Screenshot_20190526-185429.jpg)
![游戏界面](https://github.com/Mario8664/CDD/blob/master/screenshot/Screenshot_20190526-185438.jpg)


