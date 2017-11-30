#!/usr/bin/env python

import random
import os

# Script directory
main_dir = os.path.split(os.path.abspath(__file__))[0]
# Load nouns to list
with open(os.path.join(main_dir, 'RandomPhrasesVocab/noun'), 'r', encoding='utf-8') as f:
    nouns = f.read().splitlines()
# Load adjectives to list
with open(os.path.join(main_dir, 'RandomPhrasesVocab/adj'), 'r', encoding='utf-8') as f:
    adjectives = f.read().splitlines()

def randomPhrase(count=1):
    phrases = []

    for cnt in range(0, count):
        # Found random adj and noun from lists
        randNoun = random.choice(nouns)
        randAdj  = random.choice(adjectives)

        phrases.append("{} {}".format(randAdj, randNoun))

    return phrases[0] if count == 1 else phrases

if __name__ == '__main__':
    print(randomPhrase(10))
