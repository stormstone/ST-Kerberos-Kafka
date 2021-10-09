package st.kafka.kerberos;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * <p>
 * Kafka集成Kerberos生产者
 * </p>
 *
 * @author shihua
 */
public class KafkaKerberosProducer {
    /**
     * jass 配置文件路径
     */
    private static final String JASS_CONF_PATH = "/etc/kafka/conf/kafka_client_jaas.conf";
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
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // kerberos配置
        props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.mechanism", "GSSAPI");
        props.put("sasl.kerberos.service.name", "kafka");

        Producer<String, String> producer = new KafkaProducer<>(props);
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, "hello");
        producer.send(record);
        System.out.println("发送消息：" + record);
        producer.close();
    }
}
