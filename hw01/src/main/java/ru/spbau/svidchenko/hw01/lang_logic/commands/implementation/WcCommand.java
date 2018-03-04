package ru.spbau.svidchenko.hw01.lang_logic.commands.implementation;

import ru.spbau.svidchenko.hw01.common.IOUtils;
import ru.spbau.svidchenko.hw01.common.SystemInteractionApi;
import ru.spbau.svidchenko.hw01.exceptions.CliException;
import ru.spbau.svidchenko.hw01.lang_logic.commands.Command;
import ru.spbau.svidchenko.hw01.lang_logic.commands.CommandOutput;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * Word count command implementation
 * @author ArgentumWalker
 */
public class WcCommand extends Command {
    public WcCommand(List<String> arguments) {
        super(arguments);
    }

    @Override
    public CommandOutput execute(List<String> input) {
        List<String> args = getArguments();
        if (args.size() > 1) {
            return CommandOutput.interrupt();
        }
        if (args.size() == 1) {
            try (InputStream file = SystemInteractionApi.getFile(args.get(0))) {
                return CommandOutput.output(IOUtils.readFrom(file));
            } catch (CliException e) {
                return CommandOutput.interrupt();
            } catch (IOException e) {
                return CommandOutput.interrupt();
            }
        }
        return CommandOutput.output(input.stream().collect(new WcCollector()));
    }

    private class WcCollector implements Collector<String, WcCollectorAcc, List<String>> {
        @Override
        public Supplier<WcCollectorAcc> supplier() {
            return WcCollectorAcc::new;
        }

        @Override
        public BiConsumer<WcCollectorAcc, String> accumulator() {
            return (acc, s) -> {
                List<String> lines = Arrays.asList(s.split("\\n"));
                lines.forEach(line -> acc.setWordCount(acc.getWordCount() + s.split("[ \\t]+").length));
                acc.setLineCount(acc.getLineCount() + lines.size());
                acc.setBytes(acc.getBytes() + s.length() * Character.BYTES);
            };
        }

        @Override
        public BinaryOperator<WcCollectorAcc> combiner() {
            return (a1, a2) -> {
                a1.setWordCount(a1.getWordCount() + a2.getWordCount());
                a1.setBytes(a1.getBytes() + a2.getBytes());
                a1.setLineCount(a1.getLineCount() + a2.getLineCount());
                return a1;
            };
        }

        @Override
        public Function<WcCollectorAcc, List<String>> finisher() {
            return acc -> Arrays.asList(
                    "Word count: " + acc.getWordCount(),
                    "Line count: " + acc.getLineCount(),
                    "Byte count: " + acc.getBytes());
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.emptySet();
        }
    }

    private class WcCollectorAcc {
        private long wordCount = 0;
        private long lineCount = 0;
        private long bytes = 0;

        public long getWordCount() {
            return wordCount;
        }

        public void setWordCount(long wordCount) {
            this.wordCount = wordCount;
        }

        public long getLineCount() {
            return lineCount;
        }

        public void setLineCount(long lineCount) {
            this.lineCount = lineCount;
        }

        public long getBytes() {
            return bytes;
        }

        public void setBytes(long bytes) {
            this.bytes = bytes;
        }
    }
}
