package net.orfjackal.bugs;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileInputStreamReadBytesBug {

    private static final Logger logger = Logger.getLogger(FileInputStreamReadBytesBug.class.getName());

    public static void main(String[] args) throws Exception {
        int counter = 0;
        while (true) {
            Thread outThread = null;
            Thread errThread = null;
            try {
                // org.pitest.mutationtest.instrument.MutationTestUnit#runTestInSeperateProcessForMutationRange


                // *** start slave

                ServerSocket commSocket = new ServerSocket(0);
                int commPort = commSocket.getLocalPort();
                System.out.println("commPort = " + commPort);

                // org.pitest.mutationtest.execute.MutationTestProcess#start
                //   - org.pitest.util.CommunicationThread#start
                FutureTask<Integer> commFuture = createFuture(commSocket);
                //   - org.pitest.util.WrappingProcess#start
                //       - org.pitest.util.JavaProcess#launch
                Process slaveProcess = startSlaveProcess(commPort);
                outThread = new Thread(new ReadFromInputStream(slaveProcess.getInputStream()), "stdout");
                errThread = new Thread(new ReadFromInputStream(slaveProcess.getErrorStream()), "stderr");
                outThread.start();
                errThread.start();

                // *** wait for slave to die

                // org.pitest.mutationtest.execute.MutationTestProcess#waitToDie
                //    - org.pitest.util.CommunicationThread#waitToFinish
                System.out.println("waitToFinish");
                Integer controlReturned = commFuture.get();
                System.out.println("controlReturned = " + controlReturned);
                // NOTE: the following won't get called if commFuture.get() fails!
                //    - org.pitest.util.JavaProcess#destroy
                outThread.interrupt(); // org.pitest.util.AbstractMonitor#requestStop
                errThread.interrupt(); // org.pitest.util.AbstractMonitor#requestStop
                slaveProcess.destroy();
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }

            // test: the threads should exit eventually
            outThread.join();
            errThread.join();
            counter++;
            System.out.println("try " + counter + ": stdout and stderr threads exited normally");
        }
    }

    private static Process startSlaveProcess(int commPort) throws IOException {
        String separator = System.getProperty("file.separator");
        String javaProc = System.getProperty("java.home") + separator + "bin" + separator + "java";
        ProcessBuilder pb = new ProcessBuilder(javaProc, "-cp", "target/test-classes;.", "net.orfjackal.bugs.Hello", String.valueOf(commPort));
        return pb.start();
    }

    private static FutureTask<Integer> createFuture(final ServerSocket controlSocket) {
        // org.pitest.util.CommunicationThread#createFuture
        FutureTask<Integer> commFuture = new FutureTask<Integer>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                // org.pitest.util.SocketReadingCallable#call

                Socket clientSocket = controlSocket.accept();
                try {
                    InputStream in = clientSocket.getInputStream();
                    int b;
                    while ((b = in.read()) > 0) {
                        System.out.println("control read " + b);
                    }
                    in.close();
                    return b;

                } finally {
                    clientSocket.close();
                    controlSocket.close();
                }
            }
        });
        new Thread(commFuture, "communication").start();
        return commFuture;
    }

    private static class ReadFromInputStream implements Runnable {

        private final InputStream in;

        public ReadFromInputStream(InputStream in) {
            this.in = in;
        }

        @Override
        public void run() {
            byte[] buf = new byte[100];
            try {
                int len;
                while ((len = in.read(buf)) > 0) {
                    String output = new String(buf, 0, len);
                    Thread t = Thread.currentThread();
                    System.out.println("thread " + t.getName() + " " + t.getId() + ", read " + len + " bytes: " + output);
                }

            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to read", e);

            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Failed to close", e);
                }
            }
        }
    }
}
