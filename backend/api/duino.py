import requests
import os
from datetime import datetime, timedelta
from config import DUINO_USERNAME, DUINO_PASSWORD

class DucoAPIError(Exception):
    """Custom exception for Duino API errors."""
    pass

def duco_get(url: str, params: dict | None = None) -> dict:
    """Perform GET request to Duino API and return JSON or raise DucoAPIError."""
    if params is None:
        params = {}
    
    try:
        response = requests.get(url, params=params, timeout=10)
        response.raise_for_status()
        data = response.json()
        return data
    except requests.RequestException as e:
        raise DucoAPIError(f"Failed to reach Duino API: {e}")
    except ValueError:
        raise DucoAPIError("Invalid JSON from Duino API")


def get_balance(username: str = DUINO_USERNAME) -> float:
    """
    Retrieve the DUCO balance for a given username.
    
    :param username: Duino username. Defaults to account from config.
    :type username: str
    :return: Current DUCO balance.
    :rtype: float
    """

    url = f"https://server.duinocoin.com/balances/{username}"
    data = duco_get(url)
    
    if data.get("success"):
        return data["result"]["balance"]
    else:
        raise DucoAPIError(f"Duino API error: {data.get('message')}")

def get_transaction_from_hash(hash: str) -> dict:
    """
    Retrieves transaction using it's hash.
    
    :param hash: Hash of the transaction to lookup
    :type hash: str
    :return: Retrieved transaction data
    :rtype: dict[Any, Any]
    """

    url = f"https://server.duinocoin.com/transactions/{hash}"
    data = duco_get(url)

    return data

def validate_deposit(transaction_hash: str, username: str = DUINO_USERNAME) -> tuple[float, str]:
    """
    Validates deposit from hash, checks if the deposit has been redeemed.
    Reject using deposits older than 3 days.
    
    :param transaction_hash: Hash of the transaction to lookup
    :type transaction_hash: str
    :param username: Duino username of the receiving account. Defaults to account from config.
    :type username: str
    :return: Amount deposited + message
    :rtype: tuple[str, float]
    """

    transaction_data = get_transaction_from_hash(transaction_hash)

    if transaction_data.get("success"):
        date_string = transaction_data["result"]["datetime"]
        
        date = datetime.strptime(date_string, "%d/%m/%Y %H:%M:%S")

        now = datetime.now()

        if now - date > timedelta(days=3):
            return (0, "Transaction too old!")
        
        if transaction_data["result"]["recipient"] != username:
            return (0, "Transaction recipient invalid!")
        
        if transaction_data["result"]["sender"] == "Duino-Coin Masternode":
            return (0, "Transaction sender is Duino-Coin Masternode!")
        
        if check_if_line_present_and_add(transaction_hash, "hashes.txt"):
            return (0, "Transaction already credited!")

        return (transaction_data["result"]["amount"], "Transaction valid, duco should be credited.")
    
    return (0, "Transaction hash invalid!")

def check_if_line_present_and_add(line: str, filename: str) -> bool:
    """
    Checks if a line is present in the file. 
    If present, returns True. If not present, adds it to the file and returns False.
    
    :param line: The line you are checking for
    :type line: str
    :param filename: Name of the file with it's relative path if present
    :type filename: str
    :return: True if line already present, False if newly added
    :rtype: bool
    """

    if not os.path.exists(filename):
        with open(filename, "w"):
            pass

    with open(filename, "r") as f:
        file_content = f.readlines()
    
    if line+"\n" in file_content:
        return True
    
    with open(filename, "a") as f:
        f.write(line+"\n")

    return False

def withdraw(recipient_username: str, amount:float, sender_username: str = DUINO_USERNAME, sender_password: str = DUINO_PASSWORD) -> tuple[bool, str]:

    return (False, "")
    ### TODO: Implement withdrawal logic ###
    # Account got flagged (ops)
    '''{
	"message": "NO,You're using multiple accounts: XXX XXX_2, this is not allowed",
	"server": "master2",
	"success": false
    }'''
    