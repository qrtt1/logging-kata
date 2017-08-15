# monitoring lab

## 基本情境

1. 有一個 web 伺服器 (nginx)
2. 背後有一個 java web container (tomcat)
3. 有個簡單的 web application (spring boot)

我們要練習使用 fluentd 與 ELK 追蹤系統事件與蒐集量測的數據

## 軟體元件

* elasticsearch 一個 lucene base 的全文檢索服務，我們的資料存在這裡（這挺耗資源的，通常會獨立一組）
* kibana 圖形化工具介面，方便用來展示資料
* fluentd Log 蒐集程式

## 動手建環境

使用 vagrant + virtualbox 實作（這需要先安裝好）

### host os

```
vagrant up
vagrant ssh
```

### guest os

後續軟體安裝

```
cd /vagrant
ansible-playbook provision.yml
```

啟動服務（如果 vm 資源不夠，可能會卡一陣子）

```
cd /vagrant
sh start-services.sh
```

部署範例程式

```
cp demo.war /opt/apps/apache-tomcat-8.5.20/webapps/
```