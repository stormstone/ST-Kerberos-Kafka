package st.kafka.kerberos;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * <p>
 * Kafka集成Kerberos消费者
 * </p>
 *
 * @author shihua
 */
public class KafkaKerberosConsumer02 {
    /**
     * jass 配置文件路径
     */
    private static final String JASS_CONF_PATH = "/etc/kafka/conf/kafka_client_jaas_keytab.conf";
    /**
     * krb5 配置文件路径
     */
    private static final String KEB5_CONF_PATH = "/etc/krb5.conf";

    private static final String BOOTSTRAP_SERVERS = "node1.local:6667,node2.local:6667,node3.local:6667";
    private static final String TOPIC_NAME = "kafka_kerberos_test_01";

    public static void main(String[] args) {
        // 配置文件路径
        System.setProperty("java.security.auth.login.config", JASS_CONF_PATH);
        System.setProperty("java.security.krb5.conf", KEB5_CONF_PATH);

        Properties props = new Properties();
        props.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        props.put("group.id", "KafkaKerberosConsumerGroup-02");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // kerberos配置
        props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.mechanism", "GSSAPI");
        props.put("sasl.kerberos.service.name", "kafka");
        // 创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        // 订阅topic，可以为多个用,隔开，此处订阅了"test"这个主题
        consumer.subscribe(Arrays.asList(TOPIC_NAME));
        // 持续监听
        while (true) {
            // poll频率
            ConsumerRecords<String, String> consumerRecords = consumer.poll(100);
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                System.out.println("读到消息：" + consumerRecord.value());
            }
        }
    }
}
