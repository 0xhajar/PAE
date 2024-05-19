const usePathPrefix = (path) => {
    if (process.env.BUILD_MODE !== 'production') return path;
  
    let pathPrefix = process.env.PATH_PREFIX;
    if (pathPrefix.length > 1) {
      if (pathPrefix.at(-1) === '/') pathPrefix = pathPrefix.slice(0, -1);
      return pathPrefix + path;
    }
    return path;
  };
  
  
  export default usePathPrefix ;
  