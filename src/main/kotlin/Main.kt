import org.apache.pulsar.client.admin.PulsarAdmin
import org.apache.pulsar.client.api.Producer
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.Schema


fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments at Run/Debug configuration
    val client: PulsarClient = PulsarClient.builder()
        .serviceUrl("pulsar://localhost:6650")
        .build()

    val admin: PulsarAdmin = PulsarAdmin.builder()
        .serviceHttpUrl("http://localhost:38080")
        .build();

    publish_message(client)
    update_schema(admin)

    println("Program arguments: ${args.joinToString()}")
}

private fun update_schema(admin: PulsarAdmin) {
    admin.schemas().createSchema("persistent://public/default/my-topic", Schema.JSON(Person::class.java).schemaInfo)
}

private fun publish_message(client: PulsarClient) {
    val person = Person(telephone = "2324", secondName = "Second", thirdName = "Third")
    person.firstName = "First"
    person.last = "Last"
    person.age = 24
    person.address = "Como"

    val producer: Producer<Person> =
        client.newProducer(Schema.JSON(Person::class.java)).topic("persistent://public/default/my-topic").create()
    producer.newMessage().value(person).send()
}