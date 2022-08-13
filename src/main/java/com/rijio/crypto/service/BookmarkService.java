package com.rijio.crypto.service;

import com.rijio.crypto.payload.BookmarkDTO;
import com.rijio.crypto.payload.UserBookmarkResponse;

public interface BookmarkService {
    UserBookmarkResponse getAllBookmarksByUserIdPaginated(int pageNo, int pageSize, String sortBy, String sortDir);
    BookmarkDTO createBookmark(String coin);
    void deleteBookmark(Long id);
}
