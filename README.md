# logging kata

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

在 ansible 腳本內，我們也裝了 nginx，可以試著連線測試，看是否有成功連線（一開始會是 404 Not Found）

```
http://192.168.55.66
```


啟動服務（如果 vm 資源不夠，可能會卡一陣子）

```
cd /vagrant
sh start-services.sh
```

### 部署範例程式

```
cp <you-war> /opt/apps/apache-tomcat-8.5.20/webapps/
```

PS. 這個在 ansible 內有做，但如果有重新編譯的話，記得複製一下

#### Hello World

```
curl http://192.168.55.66/longQuery/
```

#### longQuery

```
curl http://192.168.55.66/longQuery/{howLong}
```

呼叫下去會 delay `howLong` ms

#### keepMemory

```
curl http://192.168.55.66/keepMemory
```

呼叫一次，增加 1 MB 記憶體用量

#### releaseMemory

```
curl http://192.168.55.66/releaseMemory
```

清空持有的記憶體用量


## fluentd

* 學習安裝 plugin ( `sh install-plugins.sh` )
* 學習設定 td-agent.conf（source, filter, match {output, store}）
  * 學習接客製的 log（使用 [regex editor](http://fluentular.herokuapp.com/) 或找社群的 plugin）
  * 參考 fluentd [source code](https://github.com/fluent/fluentd/tree/master/lib/fluent/plugin) 裡的範本

```
root@fluentd-aggregator:/etc/td-agent# td-agent-gem list | grep fluent
WARN: Unresolved specs during Gem::Specification.reset:
      msgpack (< 2, >= 0.5.11)
WARN: Clearing out unresolved specs.
Please report a bug if this causes problems.
fluent-logger (0.6.1)
fluent-mixin-config-placeholders (0.4.0)
fluent-mixin-plaintextformatter (0.2.6)
fluent-plugin-bigquery (0.4.4)
fluent-plugin-buffer-lightening (0.0.2)
fluent-plugin-elasticsearch (1.9.2)
fluent-plugin-gcs (0.3.0)
fluent-plugin-google-cloud (0.5.3)
fluent-plugin-kafka (0.4.1)
fluent-plugin-mongo (0.7.16)
fluent-plugin-rewrite-tag-filter (1.5.5)
fluent-plugin-s3 (0.8.0)
fluent-plugin-scribe (0.10.14)
fluent-plugin-secure-forward (0.4.3)
fluent-plugin-td (0.10.29)
fluent-plugin-td-monitoring (0.2.2)
fluent-plugin-ua-parser (1.1.0)
fluent-plugin-webhdfs (0.4.2)
fluentd (0.12.39)
fluentd-ui (0.4.3)
```

#### 安裝 plugins

將 log 通過安全管道傳給另一台 fluentd 的 plugin
```
sudo td-agent-gem install fluent-plugin-secure-forward
```

將 log 資料重整用的相關 plugin

```
sudo td-agent-gem install fluent-plugin-record-modifier
sudo td-agent-gem install fluent-plugin-ua-parser
```

將 log 輸出至 elasticsearch 的 plugin

```
sudo td-agent-gem install fluent-plugin-elasticsearch
```


### elasticsearch + kibana

* 學習設定 dashboard 與 visualization
* 認識 [curator](https://www.elastic.co/guide/en/elasticsearch/client/curator/5.0/index.html) 定時清 log

#### api

查詢 index

```
curl 'localhost:9200/_cat/indices?v'
```

刪除 index

```
curl -XDELETE 'localhost:9200/{index-name}'
```