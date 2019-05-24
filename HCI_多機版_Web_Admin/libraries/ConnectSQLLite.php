<?php
class MySQLite extends SQLite3
{
  function __construct($pathDB)
  {
     $this->open($pathDB);
  }
}