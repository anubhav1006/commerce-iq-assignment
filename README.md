# commerce-iq-assignment
Problem Statement:    
Build a REST based JSON mock server to easily add, update, delete and access data from a JSON file.  
Every data set should have a parent identifier (entity type), which will be used in the GET APIs.  
Every data set should have an ID (Primary key)  ID should be immutable, error needs to be thrown if ID is tried to be mutated.  
If you make POST, PUT, PATCH or DELETE requests, changes have to be automatically saved to store.json.  
The store.json file should support multiple entity types.    Sample APIs to be supported by the mock server on store.json file:  GET    /posts  GET    /posts/0  POST   /posts  PUT    /authors/1  PATCH  /posts/1  DELETE /posts/1  Enable filtering at entity level :  GET /posts?title=title1&amp;author=CIQ  Enable sorting at entity level :  GET /posts?_sort=views&amp;_order=asc  Enable basic search at entity level:  GET /posts?q=IQ  Support for nested structures will yield a bonus point.  Treat store.json as an empty slate where you can add and retrieve any data.
