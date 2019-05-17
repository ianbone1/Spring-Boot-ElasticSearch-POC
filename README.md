# ElasticSearch

## Setup

- Installed ElasticSearch v7.0.1 from https://www.elastic.co/ onto USB drive
- InteliJ 2019.1.2
- Java 1.8
- Insomnia (API testing tool)

## Steps

Following steps on article: https://dzone.com/articles/spring-boot-elasticsearch . Some of the steps in this article are out of date and a few code changes were required to make it compile and connect to the current version of ElasticSearch used in this POC.

- start ElasticSearch
```
cd elasticsearch/bin
./elasticsearch
```
- prove it works by pointing Chrome to:
```
http://localhost:9200
```

**Note browser port is 9200, Java port is 9300 for connections**

results:
```
{
"name": "Ians-MacBook-Air.local",
"cluster_name": "elasticsearch",
"cluster_uuid": "o1L_MOO-ThaaLNHrQdy-oQ",
"version": {
"number": "7.0.1",
"build_flavor": "default",
"build_type": "tar",
"build_hash": "e4efcb5",
"build_date": "2019-04-29T12:56:03.145736Z",
"build_snapshot": false,
"lucene_version": "8.0.0",
"minimum_wire_compatibility_version": "6.7.0",
"minimum_index_compatibility_version": "6.0.0-beta1"
},
"tagline": "You Know, for Search"
}
```

- creating a record in the database.

For this I used Insomnia to allow JSON format for POST:

POST request to:
```
http://localhost:9200/users/customers/
```
JSON:
```
{
  "userId" :"2",
  "name" : "SpongeBob",
   "userSettings" : {
   "gender" : "male",
   "occupation" : "CA",
   "hobby" : "Burgers"
   }
}
```
- To search for **all** documents with an index of user of type employee:

```
http://localhost:9200/users/employees/_search
```
Results:
```
{
  "took": 46,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": {
      "value": 2,
      "relation": "eq"
    },
    "max_score": 1.0,
    "hits": [
      {
        "_index": "users",
        "_type": "employee",
        "_id": "Vnx7v2oBryBplHUcy90W",
        "_score": 1.0,
        "_source": {
          "userId": "1",
          "name": "Ian",
          "userSettings": {
            "gender": "male",
            "occupation": "CA",
            "hobby": "Bikes"
          }
        }
      },
      {
        "_index": "users",
        "_type": "employee",
        "_id": "V3x_v2oBryBplHUcnN3z",
        "_score": 1.0,
        "_source": {
          "userId": "2",
          "name": "SpongeBob",
          "userSettings": {
            "gender": "male",
            "occupation": "CA",
            "hobby": "Burgers"
          }
        }
      }
    ]
  }
}
```

- to query back just SpongeBob by "\_id":
```
http://localhost:9200/users/customers/V3x_v2oBryBplHUcnN3z
```

Results:
```
{
  "_index": "users",
  "_type": "employee",
  "_id": "V3x_v2oBryBplHUcnN3z",
  "_version": 1,
  "_seq_no": 1,
  "_primary_term": 1,
  "found": true,
  "_source": {
    "userId": "2",
    "name": "SpongeBob",
    "userSettings": {
      "gender": "male",
      "occupation": "CA",
      "hobby": "Burgers"
    }
  }
}
```

- to query by a data field using JSON, e.g. "name". do a GET with the following JSON:
```
http://localhost:9200/users/employee/_search
```
JSON:
```
{"query":
 { "match":
	{"name" : "SpongeBob" }
 }
}
```

Result:
```
{
  "took": 17,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": {
      "value": 1,
      "relation": "eq"
    },
    "max_score": 0.6931472,
    "hits": [
      {
        "_index": "users",
        "_type": "employee",
        "_id": "V3x_v2oBryBplHUcnN3z",
        "_score": 0.6931472,
        "_source": {
          "userId": "2",
          "name": "SpongeBob",
          "userSettings": {
            "gender": "male",
            "occupation": "CA",
            "hobby": "Burgers"
          }
        }
      }
    ]
  }
}
```

- to query in the browser using the URL for the same:
```
http://localhost:9200/users/employee/_search?q=name:SpongeBob
```

## Java Setup

- POM file:

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.1.5.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.elastic.sample</groupId>
	<artifactId>elastic</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>elastic</name>
	<description>Demo project for Spring Boot</description>

	<properties>
		<java.version>1.8</java.version>
		<elasticsearch.version>6.7.2</elasticsearch.version> <!--This is how to set your elastic search version that Spring Boot expects -->
	</properties>

	<dependencies>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-elasticsearch</artifactId>
			<version>2.1.5.RELEASE</version>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
			<version>2.1.5.RELEASE</version>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
			<version>2.1.5.RELEASE</version>
		</dependency>

		<dependency>
			<groupId>org.elasticsearch</groupId>
			<artifactId>elasticsearch</artifactId>
		</dependency>

		<dependency>
			<groupId>org.elasticsearch.client</groupId>
			<artifactId>transport</artifactId>
		</dependency>

		<dependency>
			<groupId>org.apache.logging.log4j</groupId>
			<artifactId>log4j-api</artifactId>
			<version>2.11.2</version>
		</dependency>

		<dependency>
			<groupId>org.apache.logging.log4j</groupId>
			<artifactId>log4j-core</artifactId>
			<version>2.9.1</version>
		</dependency>

		<dependency>
			<groupId>org.apache.logging.log4j</groupId>
			<artifactId>log4j-web</artifactId>
			<version>2.7</version>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-thymeleaf</artifactId>
		</dependency>

	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<version>2.1.5.RELEASE</version>
			</plugin>
		</plugins>
	</build>

</project>

```

- application.properties file
**NB. Java API connects to ElasticSearch on 9300, if using a browser you connect on 9200**

```
# Local Elasticsearch config
elasticsearch.host=localhost
elasticsearch.port=9300
# App config
server.port=8102
spring.application.name=ElasticSample
server.error.whitelabel.enabled=false
```

- Package Structure:

Package Name : com.elastic.sample.elastic
- com.elastic.sample.elastic
 - config
   - config
 - Controller
   - UserController
 - Models
   - User
- ElasticApplication



- config file:

```
package com.elastic.sample.elastic.config;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class config{
    @Value("${elasticsearch.host:localhost}")
    public String host;
    @Value("${elasticsearch.port:9300}")
    public int port;
    public String getHost() {
        return host;
    }
    public int getPort() {
        return port;
    }
    @Bean
    public Client client(){
        TransportClient client = null;
        try{
            System.out.println("host:"+ host+"port:"+port);
            client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }
}
```

- User file:

```
package com.example.elasticsearch.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class User {

    private String userId;
    private String name;
    private Date creationDate = new Date();
    private Map<String, String> userSettings = new HashMap<>();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Map<String, String> getUserSettings() {
        return userSettings;
    }

    public void setUserSettings(Map<String, String> userSettings) {
        this.userSettings = userSettings;
    }
}

```

- UserController file:

```
package com.elastic.sample.elastic.Controller;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.elasticsearch.action.search.SearchResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;


@RestController
@RequestMapping(value="/users")
public class UserController {


    @Autowired
    Client client;
    @PostMapping("/create")
    public String create(@RequestBody com.example.elasticsearch.model.User user) throws IOException {
        IndexResponse response = client.prepareIndex("users", "employee", user.getUserId())
                .setSource(jsonBuilder()
                        .startObject()
                        .field("name", user.getName())
                        .field("userSettings", user.getUserSettings())
                        .endObject()
                )
                .get();
        System.out.println("response id:"+response.getId());
        return response.getResult().toString();
    }

    @GetMapping("/view/{id}")
    public Map<String, Object> view(@PathVariable final String id) {
        GetResponse getResponse = client.prepareGet("users", "employee", id).get();
        return getResponse.getSource();
    }

    @GetMapping("/view/name/{field}")
    public Map<String, Object> searchByName(@PathVariable final String field) {
        Map<String,Object> map = null;
        SearchResponse response = client.prepareSearch("users")
                .setTypes("employee")
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setQuery(QueryBuilders.matchQuery("name", field))
                                .get()
        ;
        List<SearchHit> searchHits = Arrays.asList(response.getHits().getHits());
        map =   searchHits.get(0).getSourceAsMap();
        return map;
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable final String id) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("users")
                .type("employee")
                .id(id)
                .doc(jsonBuilder()
                        .startObject()
                        .field("name", "Rajesh")
                        .endObject());
        try {
            UpdateResponse updateResponse = client.update(updateRequest).get();
            System.out.println(updateResponse.status());
            return updateResponse.status().toString();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e);
        }
        return "Exception";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable final String id) {
        DeleteResponse deleteResponse = client.prepareDelete("users", "employee", id).get();
        return deleteResponse.getResult().toString();
    }

}

```

- ElasticApplication file:

```
package com.elastic.sample.elastic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ElasticApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElasticApplication.class, args);
	}

}

```

## Testing The Server

- To display SpongeBob that we created earlier. In Chrome hit:

```
http://localhost:8102/users/view/name/SpongeBob
```

Results:

```
{
"userSettings": {
"occupation": "CA",
"gender": "male",
"hobby": "Burgers"
},
"name": "SpongeBob",
"userId": "2"
}
```

- To create a new employee, do POST request with JSON attached. (I used Insomnia for this)

```
POST : http://localhost:8102/users/create
```

with JSON:
```
{
  "userId" :"3",
  "name" : "Patrick",
   "userSettings" : {
   "gender" : "male",
   "occupation" : "CA",
   "hobby" : "Dreaming"
   }
}
```
