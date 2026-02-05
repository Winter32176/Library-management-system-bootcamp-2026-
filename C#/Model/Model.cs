using System.Text.Json.Serialization;

namespace Library_management_system.Model;

[JsonConverter(typeof(JsonStringEnumConverter))]
public enum BookStateDto { IN_STOCK, LOANED }

public class BookDto
{
    [JsonIgnore]
    public string IsbnOLD="";
    public string Isbn { get; set; } = "";
    public string Author { get; set; } = "";
    public string Name { get; set; } = "";
    public DateOnly Year { get; set; } = DateOnly.MinValue;
    public BookStateDto State { get; set; }

    public int numberOfLeft { get; set; } = 0;
    public int numberOfLoaned { get; set; } = 0;

    public string Section { get; set; } = "";
    public string Shelf { get; set; } = "";
}