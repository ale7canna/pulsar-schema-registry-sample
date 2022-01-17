## Summary
In this project's `Main.kt`, you'll find very simple sample codes to:
* update a schema on the pulsar schema registry;
* publish a message using the schema.

We already had some test on the schema registry. Here some useful commands/requests
to manage/check what's actually happening on pulsar. <br>
We used `ca.docker` in order to have a running pulsar instance: 
`./rebuild-dev.sh event-bus` or `./start-event-bus.sh` can help you.
For all the tests, we used a persistent
topic with `public` as tenant, `default` as namespace, and `my-topic` as name. This results in 
`persistent://public/defaul/my-topic` full topic name.

### Pulsar admin CLI
In order to access the pulsar admin CLI, just start a `bash` process inside the running pulsar container:
```shell
docker exec -it cadocker_event-bus_1 bash
```
and then change directory to `/bin`
```shell
cd /bin/
```
#### Get/Set Compatibility strategy
```shell
./pulsar-admin namespaces get-schema-compatibility-strategy public/default
./pulsar-admin namespaces set-schema-compatibility-strategy -c FORWARD_TRANSITIVE public/default
```

#### Get/Set auto-update option
```shell
./pulsar-admin namespaces get-is-allow-auto-update-schema public/default
./pulsar-admin namespaces set-is-allow-auto-update-schema --disable public/default
```

### Pulsar REST API
To use the API, just check that the container is exposing the port `38080` 
(the default we use for the admin rest interface). Start an interactive python shell, import `requests` 
and you're ready to go.

#### Get topic schema
```python
requests.get('http://localhost:38080/admin/v2/schemas/public/default/my-topic/schema').json()                                                                                                                                                                                              
```

#### Upload topic schema
```python
requests.post(
    'http://localhost:38080/admin/v2/schemas/public/default/my-topic2/schema', 
    json={'type': 'JSON', 'schema': '{"type":"record","name":"Person2","fields":[{"name":"firstName","type":["null","string"],"default":null},{"name":"last","type":["null","string"],"default":null},{"name":"country","type":["null","string"],"default":null},{"name":"city","type":["string"]}]}', 'properties': {}})           
```

#### Get topic statistics
```python
requests.get('http://localhost:38080/admin/v2/persistent/public/default/my-topic/stats').json()                                          
```

#### Get last message id
```python
requests.get('http://localhost:38080/admin/v2/persistent/public/default/my-topic/lastMessageId').json()          
```

#### Get Message by id
```python
requests.get('http://localhost:38080/admin/v2/persistent/public/default/my-topic/ledger/24/entry/1').content      
```
