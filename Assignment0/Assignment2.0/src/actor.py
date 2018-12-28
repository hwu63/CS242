from src import movie


class actor:
    def __init__(self, name_):

        if name_.find("/wiki/") >= 0:
            self.name = name_[6:]
        else:
            self.name = name_

        self.age = 1
        self.movies = set()
        self.total_gross = 0
        self.neighbors = None
        self.valid = False

    def set_age(self, age_):
        self.age = age_

    def set_gross(self, gross_):
        self.gross = gross_

    def update_gross(self):
        for movie in self.movies:
            self.total_gross += movie.gross

    def add_movie(self, movie_url):
        if movie_url not in self.movies:
            self.movies.add(movie_url)
            return True
        else:
            return False

    def add_neighbors(self, neighbors):
        for neighbor in neighbors:
            if isinstance(neighbor, movie):
                if neighbor not in self.movies:
                    self.movies.add(neighbor)
                    neighbor.neighbors.add(self)
                    return True
            else:
                return False

    def clean_up(self, movie_dict):
        prefix = '/wiki/'
        for movie in self.movies:
            url = prefix + movie.title
            if url not in movie_dict.keys():
                movie.set_title("")

    def mutual_link(self):
        #add actor to film
        for film in self.movies:
            if self not in film.actors:
                film.add_actor(self)

    def set_valid(self):
        self.valid = True

    def check_valid(self):
        return self.valid
