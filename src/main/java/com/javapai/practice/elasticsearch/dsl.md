GET /log-center/log-sl4j/_search

GET /log-center/log-sl4j/_search
{
  "query":{
    "bool":{
      "must":{
        "match":{"product":"uzone_app_openapi"}
      }
    }
  }
}