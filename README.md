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

啟動服務（如果 vm 資源不夠，可能會卡一陣子）

```
cd /vagrant
sh start-services.sh
```

部署範例程式

```
cp demo.war /opt/apps/apache-tomcat-8.5.20/webapps/
```


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

### elasticsearch + kibana

* 學習設定 dashboard 與 visualization
* 認識 [curator](https://www.elastic.co/guide/en/elasticsearch/client/curator/5.0/index.html) 定時清 log
