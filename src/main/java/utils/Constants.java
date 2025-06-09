package utils;

public class Constants {

    public static final String GET_ALL_USERS = "/User";
    public static final String GET_USER_BY_ID = "/User/{id}";
    public static final String CREATE_USER = "/user/create";
    public static final String UPDATE_USER = "/User/{id}";
    public static final String DELETE_USER = "/User/{id}";
    public static final String GET_LIST_POSTS = "/post";
    public static final String GET_POSTS_BY_USER = "/user/{id}/post";
    public static final String GET_POSTS_BY_TAG = "/tag/{id}/post";
    public static final String GET_POSTS_BY_ID = "/post/{id}";
    public static final String CREATE_POST = "/post/create";
    public static final String UPDATE_POST = "/post/{id}";
    public static final String DELETE_POST = "/post/{id}";
    public static final String GET_LIST_COMMENTS = "/comment";
    public static final String GET_COMMENTS_BY_POST = "/post/{id}/comment";
    public static final String GET_COMMENTS_BY_USER = "/user/{id}/comment";
    public static final String CREATE_COMMENT = "/comment/create";
    public static final String DELETE_COMMENT = "/comment/{id}";
    public static final String GET_TAG_LIST = "/tag";
}
