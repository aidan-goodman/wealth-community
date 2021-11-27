package top.aidan.community.entity;

/**
 * Created by Aidan on 2021/10/13 14:43
 * GitHub: github.com/huaxin0304
 * Blog: aidanblog.top
 * @author Aidan
 * 将分页处理数据进行实体封装
 */

public class Page {

    /**
     * 成员变量：
     * current: 当前页码
     * limit: 单页上限
     * rows: 数据总行数
     * path： 查询路径
     */
    private int current = 1;
    private int limit = 10;
    private int rows;
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        // 不超过 100，保证效率和阅读体验
        final int maxLimit = 100;
        if (limit >= 1 && limit <= maxLimit) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页的起始行
     *
     * @return 根据当前页和每页显示数计算
     */
    public int getOffset() {
        // current * limit - limit
        return (current - 1) * limit;
    }

    /**
     * 获取总页数
     */
    public int getTotal() {
        // rows / limit [+1]
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }

    /**
     * 获取起始页码
     */
    public int getFrom() {
        int from = current - 2;
        return Math.max(from, 1);
    }

    /**
     * 获取结束页码
     */
    public int getTo() {
        int to = current + 2;
        int total = getTotal();
        return Math.min(to, total);
    }

}
