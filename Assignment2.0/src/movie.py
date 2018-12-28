
class movie:
    def __init__(self, title):
        self.year = 1900
        self.actors = set()
        self.gross = 0
        self.neighbors = None
        self.valid = False

        if title.find("/wiki/") >= 0:
            self.title = title[6:]
        else:
            self.title = title


    def add_actor(self, actor_url):
        if actor_url not in self.actors:
            self.actors.add(actor_url)
            return True
        else:
            return False

    def set_year(self, year_):
        self.year = year_
        
    def set_gross(self, gross_):
        self.gross = gross_

    def clean_up(self, actor_dict):
        wiki = '/wiki/'
        for actor in self.actors:
            url = wiki + actor.name
            if url not in actor_dict:
                actor.set_name("")

    def mutual_link(self):
        # add movie to actor
        for actor in self.actors:
            if self not in actor.movies:
                actor.add_movie(self)

    def set_valid(self):
        self.valid = True

    def check_valid(self):
        return self.valid