package com.rijio.crypto.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.rijio.crypto.entity.Bookmark;
import com.rijio.crypto.entity.User;
import com.rijio.crypto.exception.ResourceNotFoundException;
import com.rijio.crypto.payload.BookmarkDTO;
import com.rijio.crypto.payload.UserBookmarkResponse;
import com.rijio.crypto.repository.BookmarkRepository;
import com.rijio.crypto.repository.UserRepository;
import com.rijio.crypto.service.BookmarkService;

@Service
public class BookmarkServiceImpl implements BookmarkService{

    private UserRepository userRepository;
    private BookmarkRepository bookmarkRepository;
    private ModelMapper mapper;

    public BookmarkServiceImpl(UserRepository userRepository, BookmarkRepository bookmarkRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.mapper = mapper;
    }

    @Override
    public BookmarkDTO createBookmark(String coin) {
        User user = getCreds();

        Bookmark bookmark = new Bookmark();
        bookmark.setCoin_id(coin);
        bookmark.setUser(user);
        
        Bookmark newBookmark = bookmarkRepository.save(bookmark);

        return mapToDTO(newBookmark);
    }


    @Override
    public UserBookmarkResponse getAllBookmarksByUserIdPaginated(int pageNo, int pageSize, String sortBy, String sortDir) {
        // sort
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // get user id
        User user = getCreds();
        
        Page<Bookmark> pagedBookmarks =  bookmarkRepository.findByUserId(user.getId(), pageable);
        List<Bookmark> bookmarks = pagedBookmarks.getContent();
        List<BookmarkDTO> bookmarkDTOs = bookmarks.stream().map(d -> mapToDTO(d)).collect(Collectors.toList());

        UserBookmarkResponse res = new UserBookmarkResponse();
        res.setBookmarks(bookmarkDTOs);
        res.setPageNo(pagedBookmarks.getNumber());
        res.setPageSize(pagedBookmarks.getSize());
        res.setTotalElements(pagedBookmarks.getTotalElements());
        res.setTotalPages(pagedBookmarks.getTotalPages());
        res.setLast(pagedBookmarks.isLast());

        return res;
    }

    @Override
    public void deleteBookmark(Long id) {
        Bookmark bookmark = bookmarkRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bookmark", "id", id.toString()));
        bookmarkRepository.delete(bookmark);
        
    }

    private User getCreds() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            User user = userRepository.findByUsername(currentUserName).orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUserName));
            return user;
        }

        return null;
    }

    private Bookmark mapToEntity(BookmarkDTO bookmarkDTO) {
        Bookmark bookmark = mapper.map(bookmarkDTO, Bookmark.class);
        return bookmark;
    }

    private BookmarkDTO mapToDTO(Bookmark bookmark) {
        BookmarkDTO bookmarkDTO = mapper.map(bookmark, BookmarkDTO.class);
        return bookmarkDTO;
    }

}
