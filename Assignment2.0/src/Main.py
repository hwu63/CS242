import logging

import src.jsonfy as jsonfy
import src.scraper as scraper
import os

from src import query

#os.remove("log")
logging.basicConfig(filename='log', format='%(levelname)s: %(message)s', level = logging.DEBUG)

#scrap to graph
g = scraper.scrape_to_graph('/wiki/Leonardo_DiCaprio')

#to json file
jsonfy.jsonfing(g.actors.values(), g.movies.values(), 'my_data.json')
actor_dict, movie_dict = jsonfy.restore('my_data.json')

