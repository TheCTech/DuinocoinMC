import os
from dotenv import load_dotenv
from typing import cast

load_dotenv() # Load .env

# Secrets
DUINO_USERNAME = cast(str, os.getenv("DUINO_USERNAME"))
DUINO_PASSWORD = cast(str, os.getenv("DUINO_PASSWORD"))
assert DUINO_USERNAME is not None and DUINO_PASSWORD is not None, \
    "You must set DUINO_USERNAME and DUINO_PASSWORD in the .env file"

# App constants
MAX_WITHDRAW_AMOUNT: float = 10.0
MIN_WITHDRAW_AMOUNT: float = 0.1