# ObjectDetectionBot
## 初始化工作
从 __Release__ 下载 __`coco.names`__ 和 模型 __`(.torchscript)`__ 并放在 __`/data/xtaw.objectdetectionbot/yolov5/`__ 目录下  
将模型改名为 __`model.torchscript`__  
模型共有五种，根据需要自行选择即可。文件越大，准确度越高，速度越慢  
## 使用方法
在群中发送 __`@Bot + 待检测图片`__  
Bot 便会自动完成检测，并将结果发送在群中  
可在一条信息中发送多张图片
## 配置文件
文件名 __`/config/xtaw.objectdetectionbot/ObjectDetection.json`__  
第一次启动时会自动生成  

示例:  
```
{  
  "Bots": [  
    123456,  
    654321  
  ],  
  "Groups": [  
    123456789,  
    987654321  
  ],  
  "Format": "%AT%\n%IMAGE%\nyolo v5 测试成功 请不要回复\n 用时: %USED_TIME%ms",  
  "UseGpu": false  
}
```  
`Bots` 中填写 __机器人登录的qq号__，包含在其中的qq号才会执行目标检测  
`Groups` 中填写 __你想执行目标检测的群号__，机器人只有在包含在其中的群中被@时才会执行目标检测  
`Format` 中填写 __机器人回复格式__，一般默认即可，`\n` 代表 __换行__， `%AT%` 代表 __@执行者__， `%IMAGE%` 代表 __检测后返回的图片__， `%USED_TIME` 代表  __检测耗时__，单位为 __ms__  
`UseGpu` 中填写 __true或false__，若没有gpu则一定填写 __false__。默认建议 __启用gpu__
## 命令
```
/objectdetection usegpu <state> # 设置是否使用 Gpu, <state> 填写 true / false
/objectdetection bot <add/remove> <id> # 添加和删除 Bots
/objectdetection group <add/remove> <id> # 添加和删除 Groups
/objectdetection format <format> # 设置 Format
/objectdetection list # 显示 Bots 和 Groups 列表
/objectdetection reload # 重新加载配置
```
`/objectdetection` 可缩写为 `/od`  
`add` 可缩写为 `a`  
`remove` 可缩写为 `r`