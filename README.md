# Alkemy Kotlin Challenge

### Overview

This is my implementation of the [Kotlin Challenge provided by Alkemy](https://drive.google.com/file/d/19sWXyPTBQ-dTeov779GZyid572iu9c0o/view).

This app is a simple movie catalog that lets you browse through the most popular movies provided by [TheMovieDataBase API](https://www.themoviedb.org/documentation/api) and select one to look at its details such as rating, release date, and more.

### Screens

#### Popular Movies Catalog

The list of movies are shown as a list of card views. Each item on the list shows the title and poster image of a movie.

The user can scroll through the list in the view to explore its content. 

<p align="center">
    <img src="https://github.com/franyanes/alkemy-kotlin-challenge/blob/main/gifs/main_activity_view_v2.gif" style="zoom:50%;" />
</p>

#### Movie Details

Displays a screen with useful information about the movie.

<p align="center">
    <img src="https://github.com/franyanes/alkemy-kotlin-challenge/blob/main/gifs/movie_details_view_v2.gif" style="zoom:50%;" />
</p>

### Technical Details

* Used [MVVM Architectural Pattern](https://www.geeksforgeeks.org/mvvm-model-view-viewmodel-architecture-pattern-in-android/).
* Used [Retrofit](https://square.github.io/retrofit/) and [RxJava](https://github.com/ReactiveX/RxJava) for fetching the information from the API.
* Used [Paging Library](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) for pagination in the Movie Catalog.