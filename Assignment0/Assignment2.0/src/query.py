import logging

from src import jsonfy


class query:
    def __init__(self, filename_):
        self.filename = filename_
        self.actor_dict, self.movie_dict = jsonfy.restore(filename_)

    # Find how much a movie has grossed
    def find_movie_gross(self, movie_):
        if movie_ is None or movie_ not in self.movie_dict:
            logging.warning("Movie not exist")
            return 0
        else:
            return self.movie_dict[movie_].gross

    # List which movies an actor has worked in
    def movie_list_of_actor(self, actor_ ):
        if actor_ is None or actor_ not in self.movie_dict:
            logging.warning("Actor not exist")
            return []
        else:
            return list(self.actor_dict[actor_].movies)

    # List which actors worked in a movie
    def actor_list_of_movie(self, movie_):
        if movie_ is None or movie_ not in self.movie_dict:
            logging.warning("Movie not exist")
            return []
        else:
            return list(self.movie_dict[movie_].actors)

    # List the top X actors with the most total grossing value
    def list_top_x_actors_by_gross(self, x):
        if x <= 0 or x > len(self.actor_dict):
            logging.warning("x is too large.")
            return []
        else:
            actor_list = self.actor_dict.values()
            #https://stackoverflow.com/questions/613183/how-to-sort-a-dictionary-by-value
            sorted(actor_list, key=lambda y: y.total_gross)
            return list(actor_list)[-1*x:]

    # List the oldest X actors
    def find_oldest_x_actors(self, x):
        if x <= 0 or x > len(self.actor_dict):
            logging.warning("x is too large.")
            return []
        else:
            actor_list = self.actor_dict.values()
            #https://stackoverflow.com/questions/613183/how-to-sort-a-dictionary-by-value
            sorted(actor_list, key=lambda y: y.age)
            return list(actor_list)[-1*x:]

    # List all the movies for a given year
    def list_movies_by_year(self, year):
        if year < 1900:
            logging.warning("pls enter later year.")
            return []
        else:
            ret = []
            for item in self.movie_dict:
                if item.year == year:
                    ret.append(item)
            if len(ret) == 0:
                logging.info("No movie this year QAQ")
            return ret

    # List all the actors for a given year
    def list_actors_by_age(self, year):
        if year <= 0:
            logging.warning("Not born yet.")
            return []
        else:
            ret = []
            age_ = 2017 - year
            for item in self.actor_dict.keys():
                print(item)
                if item.age == age_:
                    ret.append(item)
            if len(ret) == 0:
                logging.info("No actor this year QAQ")
            return ret
