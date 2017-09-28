package com.phantom.plane.core.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ClosableResourcePool {
    private static ThreadLocal<Stack<ClosableResource>> resourceThread = new ThreadLocal<Stack<ClosableResource>>();

    /**
     * 如果当前线程有栈,则从栈中获取新的map，否则返回一个新map
     *
     * @return
     */
    private static ClosableResource getClosableResource() {
        Stack<ClosableResource> taskStack = resourceThread.get();
        if (taskStack == null) {
            ClosableResource resource = new ClosableResource();
            taskStack = new Stack<ClosableResource>();
            resourceThread.set(taskStack);
            taskStack.push(resource);
            return resource;
        }
        if (taskStack.isEmpty()) {
            taskStack.push(new ClosableResource());
        }
        return taskStack.peek();
    }

    /**
     * 清楚所有线程缓存信息
     */
    public static void closeAll() {
        Stack<ClosableResource> object = resourceThread.get();
        if (object != null && !object.isEmpty()) {
            ClosableResource pop = object.pop();
            pop.closeAll();
        }
    }

    public static void addStack() {
        Stack stack = resourceThread.get();
        if (stack == null) {
            stack = new Stack();
            resourceThread.set(stack);
        }
        stack.push(new ClosableResource());
    }

    public static void add(ResultSet value) {
        getClosableResource().addResultSet(value);
    }

    public static void add(Connection value) {
        getClosableResource().addConnection(value);
    }

    static class ClosableResource {
        private List<Connection> connList = new ArrayList<Connection>();
        private List<ResultSet> rsList = new ArrayList<ResultSet>();

        public void closeAll() {
            for (ResultSet rs : rsList) {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            for (Connection conn : connList) {
                try {
                    if (conn != null && !conn.isClosed()) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }

        public void addResultSet(ResultSet value) {
            rsList.add(value);
        }

        public void addConnection(Connection value) {
            connList.add(value);
        }
    }
}