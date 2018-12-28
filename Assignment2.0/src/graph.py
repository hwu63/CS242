from src import movie
from src import actor


class graph:
    def __init__(self):
        self.actors = {}
        self.movies = {}
        self.vertices = {}

    def add_actor(self, actor_url):
        self.actors[actor_url] = actor_url

    def add_movie(self, movie_url):
        self.movies[movie_url] = movie_url

    def add_vertices(self, vertices):
        for vertex in vertices:
            if isinstance(vertex, actor.actor) or isinstance(vertex, movie.movie):
                self.vertices[vertex] = vertex.neighbors

    def add_edge(self, vertex_1, vertex_2):
        if isinstance(vertex_1, movie.movie) and isinstance(vertex_2, actor.actor):
            vertex_1.add_actor(vertex_2)
            self.actors[vertex_2] = vertex_1.actors
            self.movies[vertex_1] = vertex_2.movies

        elif isinstance(vertex_1, actor.actor) and isinstance(vertex_2, movie.movie):
            vertex_1.add_movie(vertex_2)
            self.actors[vertex_1] = vertex_1.movies
            self.movies[vertex_2] = vertex_2.actors

    # def add_edges(self, edges):
    #     for edge in edges:
    #         self.add_edge(edge[0], edge[1])

    def adjacency_list(self) :
        if len(self.actors) >= 1:
            return [str(key) + ":" + str(self.actors[key]) for key in self.actors]
        else:
            return dict()
