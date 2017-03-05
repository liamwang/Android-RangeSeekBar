# Android-RangeSeekBar

RangeSeekBar 是一个支持选择区间的 Android 组件。

![](https://raw.githubusercontent.com/liamwang/Android-RangeSeekBar/master/demo.gif)

## 引入

Maven:

```xml
<dependency>
  <groupId>com.exblr</groupId>
  <artifactId>range-seek-bar</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

Gradle:

```groovy
compile 'com.exblr:range-seek-bar:1.0.0'
```

## 如何使用

把 RangeSeekBar 添加到你的 Layout 中：

```xml
<com.exblr.rangeseekbar.RangeSeekBar
    android:id="@+id/range_seek_bar"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```

在对应的 Activity 或 Fragment 中设置刻度数组值：

```java
RangeSeekBar range = (RangeSeekBar) findViewById(R.id.range_seek_bar);
range.setScales(scales);
```

这样就可以了。

## 公开方法说明

* setOnRangeChanged: 设置选择区间发生变化的事件监听
* setOnScaleTextFormat: 用来自定义刻度文字的显示
* setThumbSize: 设置拖动控制点的大小
* setScales: 设置刻度数据
* setScaleTextPaddingTop: 设置刻度文字的上边距
* setScaleTextSize: 设置刻度文字大小
* setScaleTextColor: 设置刻度文字颜色
* setLineColorNormal: 设置拖动条常态颜色
* setLineColorSelection: 设置拖动条被选状态颜色
* getScales: 获取刻度数据
* getMax: 获取选择的最大值
* getMin: 获取选择的最小值
* getLimitMax: 获取刻度的最小值
* getLimitMin: 获取刻度的最大值


这是一个小 Widget，只有一个文件，仅200多行代码，若要深入了解请直接查看示例代码或源文件。
