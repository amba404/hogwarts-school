package ru.hogwarts.school.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.NotFoundException;

import java.util.stream.LongStream;
import java.util.stream.Stream;


@Service
public class InfoService {
    @Value("${server.port}")
    private String port;

    Logger logger = LoggerFactory.getLogger(InfoService.class);

    public String getPort() {
        logger.info("getPort: {}", port);

        return port;
    }

    public long getSumOf1M(String method, long startFrom, long endAt) {
        logger.info("getSumOf1M {}...", method);

        long result;

        long startTime = System.nanoTime();

        result = switch (method) {
            case "original" -> getSumOf1M_original(startFrom, endAt);
            case "faster" -> getSumOf1M_faster(startFrom, endAt);
            case "math" -> getSumOf1M_math(startFrom, endAt);
            default -> {
                logger.error("getSumOf1M: неизвестный метод {}", method);
                throw new NotFoundException("Неизвестный метод " + method);
            }
        };

        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / Math.pow(10, 6);

        logger.info("getSumOf1M: ответ {} получен за {} ms методом {}", result, duration, method);

        return result;
    }

    private long getSumOf1M_faster(long startFrom, long endAt) {
        return LongStream.range(startFrom, endAt + 1L).parallel()
                .reduce(0L, Long::sum);
    }

    private long getSumOf1M_math(long startFrom, long endAt) {
        return (endAt - startFrom + 1) * (startFrom + endAt) / 2L;
    }

    private long getSumOf1M_original(long startFrom, long endAt) {
        return Stream.iterate(startFrom, a -> a + 1L)
                .limit(endAt - startFrom + 1L)
                .reduce(0L, Long::sum);
    }
}
