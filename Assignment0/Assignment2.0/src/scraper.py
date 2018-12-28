import logging
import sys
import urllib.error
import urllib.request
from collections import deque

from src import actor as A
from src import graph
from src import movie as M
from bs4 import BeautifulSoup as bs

from src import util


def remove(g, url_):
    if url_ in g.movies.keys():
        del g.movies[url_]
    elif url_ in g.actors.keys():
        del g.actors[url_]

def scrape_to_graph(starting_page_name):
    #starting_page_name: /wiki/Morgan_Freeman

    wiki = "https://en.wikipedia.org"

    q = deque()
    g = graph.graph()
    q.append(starting_page_name)


    while len(g.actors) < 250 or len(g.movies) < 125:
        if len(q) <= 0:
            q.append('/wiki/Johnny_Depp')
        url = q.popleft()
        logging.info("Current Page:" + wiki + url + "...")
        if (url in g.actors.keys() and g.actors[url].check_valid()) or (url in g.movies.keys() and g.movies[url].check_valid()):
            logging.info(url + " been processed. Continue.")
            continue
        try:
            try_url = urllib.request.urlopen(wiki + url)
        except ValueError:
            logging.fatal(wiki+ url + " have value error")
            remove(g, url)
            sys.exit()
        except (urllib.error.HTTPError, urllib.error.URLError):
            logging.error( wiki + url + " not found")
            remove(g, url)
            continue

        html = try_url.read()
        soup = bs(html, "html.parser")
        name = soup.title

        if util.is_actor(soup):
            logging.info(str(name) + " is actor ")
            scrape_actor(g, q, soup, url)


        elif util.is_movie(soup):
            logging.info(str(name)+ " is movie")
            scrape_movie(g, q, soup, url)

        else:
            logging.warning(str(name)+ " is useless. :(")
            remove(g, url)

    for actor in g.actors:
        g.actors[actor].mutual_link()
        g.actors[actor].update_gross()
    for film in g.movies:
        g.movies[film].mutual_link()

    logging.info(str(len(g.actors)) + "actors and " + str(len(g.movies)) + " movies found.")

    return g

def scrape_movie(g, q, soup, url):
    if url not in g.movies:
        g.movies[url] = M.movie(url)
        g.movies[url].set_gross(util.grab_grossing_from_page(soup))
        g.movies[url].set_year(util.get_movie_year(soup))

    if len(g.actors) <= 250:
        for actor_ in util.get_cast(soup):
            if actor_ not in g.actors:
                logging.info("New actor: " + actor_)
                g.actors[actor_] = A.actor(actor_)
            q.append(actor_)
            g.movies[url].add_actor(g.actors[actor_])
    g.movies[url].set_valid()


def scrape_actor(g, q, soup, url):
    if url not in g.actors:
        g.actors[url] = A.actor(url)
    g.actors[url].set_age(util.grab_age_from_page(soup))
    list_movies = util.get_filmography(url, soup)
    if list_movies is None:
        g.actors[url].set_valid()
        return
    if len(g.movies) <= 120:
        for movie_ in list_movies:
            if movie_ not in g.movies:
                logging.info("New movie:" + movie_)
                g.movies[movie_] = M.movie(movie_)
            q.append(movie_)
            g.actors[url].add_movie(g.movies[movie_])
    g.actors[url].set_valid()

