using Library_management_system.Model;
using System.Net.Http.Json;

namespace Library_management_system.Api;

public class BooksApi
{
    private readonly HttpClient _http;

    public BooksApi(HttpClient http) => _http = http;

    public async Task<List<BookDto>?> GetAllAsync()
    {
        var b=await _http.GetFromJsonAsync<List<BookDto>>("books");
        return b;
    }

    public async Task<BookDto?> GetByIsbnAsync(string isbn)
        => await _http.GetFromJsonAsync<BookDto>($"books/{isbn}");


    public async Task<List<BookDto>?> GetByShelfAsync(string shelf)
        => await _http.GetFromJsonAsync<List<BookDto>>($"search/shelf/{shelf}");


    public async Task<List<BookDto>?> GetBySectionAsync(string section)
        => await _http.GetFromJsonAsync<List<BookDto>>($"search/section/{section}");

    public async Task DeleteBook(string isbn)
        => await _http.DeleteAsync($"delete/{isbn}");

    public async Task UpdateBook(BookDto book, string isbn)
    =>  await _http.PutAsJsonAsync<BookDto>($"update/{isbn}", book);

    public async Task AddAsync(BookDto book)
        => await _http.PostAsJsonAsync("add", book);
}