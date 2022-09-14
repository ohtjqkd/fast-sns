package com.fastcampus.fastsns.service;

import com.fastcampus.fastsns.exception.ErrorCode;
import com.fastcampus.fastsns.exception.FastSnsApplicationException;
import com.fastcampus.fastsns.model.AlarmArgs;
import com.fastcampus.fastsns.model.AlarmType;
import com.fastcampus.fastsns.model.Comment;
import com.fastcampus.fastsns.model.Post;
import com.fastcampus.fastsns.model.entity.*;
import com.fastcampus.fastsns.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final LikeEntityRepository likeEntityRepository;
    private final CommentEntityRepository commentEntityRepository;
    private final AlarmEntityRepository alarmEntityRepository;

    @Transactional
    public void create(String title, String body, String userName) {
        UserEntity userEntity = getUserEntityOrException(userName);

        postEntityRepository.save(PostEntity.of(title, body, userEntity));
    }

    @Transactional
    public Post modify(String title, String body, String userName, Integer postId) {
        PostEntity postEntity = getPostEntityOrException(postId);
        UserEntity userEntity = getUserEntityOrException(userName);

        checkPermission(postEntity, userEntity);

        postEntity.setTitle(title);
        postEntity.setBody(body);

        return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
    }

    @Transactional
    public void delete(String userName, Integer postId) {
        PostEntity postEntity = getPostEntityOrException(postId);
        UserEntity userEntity = getUserEntityOrException(userName);

        checkPermission(postEntity, userEntity);

        postEntityRepository.delete(postEntity);
    }

    public Page<Post> list(Pageable pageable) {
        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> my(String userName, Pageable pageable) {
        UserEntity userEntity = getUserEntityOrException(userName);
        return postEntityRepository.findAllByUser(userEntity, pageable).map(Post::fromEntity);
    }

    @Transactional
    public void like(Integer postId, String userName) {
        PostEntity postEntity = getPostEntityOrException(postId);
        UserEntity userEntity = getUserEntityOrException(userName);

        likeEntityRepository.findByUserAndPost(userEntity, postEntity).ifPresent(it -> {
                    throw new FastSnsApplicationException(ErrorCode.ALREADY_LIKE, String.format("userName %s already like post %s", userName, postId));
                });
        likeEntityRepository.save(LikeEntity.of(userEntity, postEntity));
        alarmEntityRepository.save(AlarmEntity.of(postEntity.getUser(), AlarmType.NEW_LIKE_ON_POST, new AlarmArgs(userEntity.getId(), postEntity.getId())));
    }

    public Integer likeCount(Integer postId) {
        PostEntity postEntity = getPostEntityOrException(postId);
        return likeEntityRepository.countByPost(postEntity);
    }

    @Transactional
    public void comment(Integer postId, String userName, String comment) {
        PostEntity postEntity = getPostEntityOrException(postId);
        UserEntity userEntity = getUserEntityOrException(userName);

        commentEntityRepository.save(CommentEntity.of(userEntity, postEntity, comment));
        alarmEntityRepository.save(AlarmEntity.of(postEntity.getUser(), AlarmType.NEW_COMMENT_ON_POST, new AlarmArgs(userEntity.getId(), postEntity.getId())));
    }

    public Page<Comment> getComments(Integer postId, Pageable pageable) {
        return commentEntityRepository.findAllByPost(getPostEntityOrException(postId), pageable).map(Comment::fromEntity);
    }

    public PostEntity getPostEntityOrException(Integer postId) {
        return postEntityRepository.findById(postId).orElseThrow(() ->
                new FastSnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));
    }

    public UserEntity getUserEntityOrException(String userName) {
        return userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new FastSnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
    }

    public void checkPermission(PostEntity postEntity, UserEntity userEntity) {
        if (postEntity.getUser() != userEntity)
            throw new FastSnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userEntity.getUserName(), postEntity.getId()));
    }


}
