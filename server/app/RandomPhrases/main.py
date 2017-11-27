#!/usr/bin/env python

import random
import os

def randomPhrase(nounList, adjList, count=1):
    phrases = []

    for cnt in range(0, count):
        # Found random adj and noun from lists
        randNoun = random.choice(nounList)
        randAdj  = random.choice(adjList)

        phrases.append("{} {}".format(randAdj, randNoun))

    return phrases

if __name__ == '__main__':
    # Script directory
    main_dir = os.path.split(os.path.abspath(__file__))[0]

    # Load nouns to list
    with open(os.path.join(main_dir, 'vocab/noun'), 'r', encoding='utf-8') as f:
        nouns = f.read().splitlines()

    # Load adjectives to list
    with open(os.path.join(main_dir, 'vocab/adj'), 'r', encoding='utf-8') as f:
        adjectives = f.read().splitlines()

    phrase = randomPhrase(nouns, adjectives, 10)
    print(phrase)