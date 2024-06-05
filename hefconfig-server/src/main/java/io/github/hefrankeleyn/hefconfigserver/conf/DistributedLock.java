package io.github.hefrankeleyn.hefconfigserver.conf;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Date 2024/6/5
 * @Author lifei
 */
@Component
public class DistributedLock {

    @Resource
    private DataSource dataSource;
    private final AtomicBoolean locked = new AtomicBoolean(false);
    private Connection connection;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @PostConstruct
    public void init() {
        try {
            connection = dataSource.getConnection();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        executor.scheduleWithFixedDelay(this::tryLock, 1000, 5000, TimeUnit.MILLISECONDS);
    }

    private boolean lock() {
        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.createStatement().execute("set innodb_lock_wait_timeout=5");
            // 第一个执行，返回tre， 第二个执行 卡住5秒，超时包异常
            connection.createStatement().execute("select lname from locks where lid=1 for update");
            if (locked.get()) {
                System.out.println("===> reenter this distributed lock...");
            } else {
                System.out.println("===> get a new distributed lock...");
            }
            return true;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void tryLock() {
        try {
            lock();
            locked.set(true);
        }catch (Exception e) {
            System.out.println("===> lock failed");
            locked.set(false);
        }
    }

    @PreDestroy
    public void colse() {
        try {
            if (Objects.nonNull(connection) && !connection.isClosed()) {
                connection.rollback();
                connection.close();
            }
        }catch (Exception e) {
            // ignore
            System.out.println("===> ignore close exception");
        }
    }

    public boolean getLocked() {
        return locked.get();
    }
}
