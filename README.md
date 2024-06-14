# Intelligent Scheduling Pomodoro Timer

`IntelligentSchedulingPomodoroTimer` 是一個基於番茄工作法的時間管理工具。它允許用戶設定一個工作時間段，然後在這段時間結束後提醒用戶休息。這個工具的主要目的是幫助用戶提高工作效率，並防止長時間工作導致的疲勞。

這個工具的主要功能包括：

1. **計時器**：用戶可以設定一個時間段，例如25分鐘，然後開始計時。計時器會在時間結束後提醒用戶休息。

2. **智慧排程**：根據用戶的需求自動安排工作和休息時間，幫助用戶在一天中分配時間更有效率。

3. **智慧建議**：根據用戶的歷史數據，提供個性化建議，例如最佳工作時間段、適當的休息時間長度等。

4. **鄰近時間提醒**：在工作時間即將結束時提前提醒用戶，讓用戶能夠更好地準備結束當前任務。

這些功能共同作用，幫助用戶更好地管理時間，提高工作效率，並保持良好的工作和休息平衡。

## How to Run

```bash
java --module-path "C:/Program Files/Java/javafx-sdk-22.0.1/lib" --add-modules javafx.controls,javafx.fxml -jar out/artifacts/IntelligentSchedulingPomodoroTimer_jar/Java_final_project_JavaFX.jar
```

請確認引號內是您的javaFX安裝路徑，以及更改指令中jar檔的位置

## Demo Video

https://www.youtube.com/watch?v=aTwDqQUsRTc

## API Key

此為本專案使用的[API](https://github.com/chatanywhere/GPT_API_free)使用前將環境變數名稱設為`GPT_API_KEY`內容為這個專案申請的API Key
