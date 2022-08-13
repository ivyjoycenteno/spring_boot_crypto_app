package com.rijio.crypto.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rijio.crypto.payload.UserBookmarkResponse;
import com.rijio.crypto.service.BookmarkService;
import com.rijio.crypto.util.AppConstants;

@RestController
@RequestMapping("/api/v1/bookmarks")
public class BookmarkController {
    private BookmarkService bookmarkService;

    @GetMapping
    public UserBookmarkResponse getAllBookmarks(
        @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
        @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
        @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {

        return bookmarkService.getAllBookmarksByUserIdPaginated(pageNo, pageSize, sortBy, sortDir);
    }
}
