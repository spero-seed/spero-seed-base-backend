package com.speroseed.http;

import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;


public class HttpCachingRequestWrapper extends ContentCachingRequestWrapper {

    // 用来区分首次读取还是非首次
    private final AtomicBoolean isFirst = new AtomicBoolean(true);

    public HttpCachingRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public HttpCachingRequestWrapper(HttpServletRequest request, int contentCacheLimit) {
        super(request, contentCacheLimit);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        if(isFirst.get()){
            //首次读取直接调父类的方法，这一次执行完之后 缓存流中有数据了
            //后续读取就读缓存流里的。
            isFirst.set(false);
            return super.getInputStream();
        }

        //用缓存流构建一个新的输入流
        return new SperoServletInputStream(super.getContentAsByteArray());
    }

    //参考自 DelegatingServletInputStream
    static class SperoServletInputStream extends ServletInputStream {

        private InputStream sourceStream;

        private boolean finished = false;

        public SperoServletInputStream(byte [] bytes) {
            //构建一个普通的输入流
            this.sourceStream = new ByteArrayInputStream(bytes);
        }

        @Override
        public int read() throws IOException {
            int data = this.sourceStream.read();
            if (data == -1) {
                this.finished = true;
            }
            return data;
        }

        @Override
        public int available() throws IOException {
            return this.sourceStream.available();
        }

        @Override
        public void close() throws IOException {
            super.close();
            this.sourceStream.close();
        }

        @Override
        public boolean isFinished() {
            return this.finished;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            throw new UnsupportedOperationException();
        }
    }
}
