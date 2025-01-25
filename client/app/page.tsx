import Warehouses from "./(warehouses)/warehouses";

export default async function Home() {
    return (
        <>
            <p className="mt-12 mb-6 w-full font-bold text-center text-2xl">
                Wybierz magazyn
            </p>
            <div className="flex flex-wrap mx-[300px] place-content-center gap-4">
                <Warehouses />
            </div>
        </>
    );
}
